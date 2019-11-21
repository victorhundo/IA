package IA;
import org.apache.bcel.Const;
import org.apache.commons.math3.*;
import org.apache.commons.math3.distribution.EnumeratedIntegerDistribution;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import robocode.*;
import org.apache.commons.math3.*;
import robocode.Robot;

import java.awt.*;
import java.util.prefs.Preferences;

//import java.awt.Color;

// API help : https://robocode.sourceforge.io/docs/robocode/robocode/Robot.html

/**
 * HAL9001 - a robot by (your name here)
 */
public class HAL9001 extends AdvancedRobot
{
    /**
     * run: HAL9001's default behavior
     */
    private GameStatus game;
    private QLearning ql;
    private Rewards rewards;
    private boolean isOptimalPolicy;

    public HAL9001(){
        State.init();
        this.ql = new QLearning();
        this.rewards = new Rewards();
        this.game = new GameStatus();
        game.setAmIAlive(true);
        game.setEnemyEnergy(100);
        State.addInitial();
        init();
    }

    private void init() {
        initAheadAction();
        initBackAction();
        initTurnLeftAction();
        initTurnRightAction();
        ql.init();
    }

    public void onPaint(Graphics2D g) {
        // Set the paint color to a red half transparent color
        g.setColor(new Color(0xff, 0x00, 0x00, 0x80));

        // Draw a line from our robot to the scanned robot
        g.drawLine((int)game.getEnemyX(), (int)game.getEnemyY(), (int)game.getX(), (int)game.getY());

        // Draw a filled square on top of the scanned robot that covers it
        g.fillRect((int) game.getEnemyX() - 20, (int) game.getEnemyY() - 20, 40, 40);
    }

    private void turnRight() {
        this.setTurnRight(Constants.TURN_ANGLE);
        this.execute();
        this.waitFor(new TurnCompleteCondition(this));
    }

    private void fire() {
        this.setFire(Constants.FIRE_POWER);
        this.execute();
        this.waitFor(new TurnCompleteCondition(this));
    }

    private void turnLeft() {
        this.setTurnLeft(Constants.TURN_ANGLE);
        this.execute();
        this.waitFor(new TurnCompleteCondition(this));
    }

    private void goBack() {
        this.setBack(Constants.MOVE_DISTANCE);
        this.execute();
        this.waitFor(new MoveCompleteCondition(this));
    }

    private void goAhead() {
        this.setAhead(Constants.MOVE_DISTANCE);
        this.execute();
        this.waitFor(new MoveCompleteCondition(this));
    }

    private void initTurnRightAction() {
        Executable turnRightAction = () -> {
            turnRight();
        };
        ql.setActionFunction(Action.TURN_RIGHT, turnRightAction);
    }

    private void initTurnLeftAction() {
        Executable turnLeftAction = () -> {
            turnLeft();
        };
        ql.setActionFunction(Action.TURN_LEFT, turnLeftAction);
    }

    private void initBackAction() {
        Executable backAction = () -> {
            goBack();
        };
        ql.setActionFunction(Action.BACK, backAction);
    }


    private void initAheadAction() {
        Executable aheadAction = () -> {
            goAhead();
        };
        ql.setActionFunction(Action.AHEAD, aheadAction);
    }


    public void run() {
        // Gun, radar and tank movements are independent
        setAdjustGunForRobotTurn(true);
        setAdjustRadarForGunTurn(true);
        setAdjustRadarForRobotTurn(true);
        setTurnRadarRightRadians(Double.POSITIVE_INFINITY);

        this.updateIsOptimalPolicy();
        while(true) {
            State state = State.updateState(game, ql);
            Action action = chooseAction(state);
            action.execute();
            this.livingReward();
            this.distanceToEnemyReward();

            // Prevents updating q-table after the end of the round.
            if (game.isAmIAlive() == false || game.getMyEnergy() == 0)
                break;
            if (!isOptimalPolicy) {
                State nextState = State.updateState(game, ql);
                ql.updateQ(state, action, nextState, rewards);
            }
            rewards.endOfCycle();
            game.resetDataAtTheEndOfCycle();
        }
    }

    private Action chooseAction(State state) {
        if (isOptimalPolicy) {
            return ql.bestAction(state);
        }
        return ql.nextAction(state, game.getRoundNum());
    }

    public void livingReward() {
        rewards.addReward(RewardType.LIVING_REWARD);
    }

    private void distanceToEnemyReward() {
        double tooClose = 50;
        double close = 200;
        if (game.getDistanceToEnemy() < tooClose) {
            rewards.addReward(RewardType.DISTANCE_TO_ENEMY_LESS_THAN_50);
        } else if (game.getDistanceToEnemy() < close) {
            rewards.addReward(RewardType.DISTANCE_TO_ENEMY_LESS_THAN_200);
        }
    }

    public void onDeath(DeathEvent e) {
//        LOG.info("Round ended");
//        rewards.endOfRound();
        game.setAmIAlive(false);
    }

    @Override
    public void onRoundEnded(RoundEndedEvent event) {
        super.onRoundEnded(event);
        rewards.endOfRound();
    }


    /**
     * onScannedRobot: What to do when you see another robot
     */
    public void onScannedRobot(ScannedRobotEvent e) {
        setTurnRadarLeftRadians(getRadarTurnRemainingRadians());

        // Update enemy energy
        game.setEnemyEnergy(e.getEnergy());

        // Update angle to enemy
        game.setAngleToEnemy(e.getBearing());

        // Calculate the angle to the scanned robot
        double angle = Math.toRadians((game.getHeading() + game.getAngleToEnemy()) % 360);

        // Calculate the coordinates of the robot
        double tempEnemyX = (game.getX() + Math.sin(angle) * e.getDistance());
        double tempEnemyY = (game.getY() + Math.cos(angle) * e.getDistance());

        if (tempEnemyX != game.getEnemyX() || tempEnemyY != game.getEnemyY()) {
            game.setEnemyX(tempEnemyX);
            game.setEnemyY(tempEnemyY);
        }

        double absoluteBearing = getHeadingRadians() + e.getBearingRadians();
        setTurnGunRightRadians(
                robocode.util.Utils.normalRelativeAngle(absoluteBearing -
                        getGunHeadingRadians()));
        // Replace the next line with any behavior you would like

        if (getGunHeat() == 0) {
            if (e.getDistance() < 50 && game.getMyEnergy() > 50 )
                fire(3);
                // otherwise, fire 1.
            else
                fire(1);
        }
    }

    /**
     * onHitByBullet: What to do when you're hit by a bullet
     */
    public void onHitByBullet(HitByBulletEvent e) {
        // Replace the next line with any behavior you would like
        int baseValue = 0;
        rewards.addReward(RewardType.HIT_BY_BULLET);
    }

    public void onHitRobot(HitRobotEvent e) {
        // check if we kill a robot
        double damage = game.getEnemyEnergy() - e.getEnergy();
        if (e.getEnergy() <= 0) {
            rewards.addReward(RewardType.COLLISION_AND_KILL_ENEMY);
        } else {
            rewards.addReward(RewardType.COLLISION_WITH_ENEMY);
        }
    }

    /**
     * onHitWall: What to do when you hit a wall
     */
    public void onHitWall(HitWallEvent e) {
        // Replace the next line with any behavior you would like
        rewards.addReward(RewardType.HIT_A_WALL);
//        back(20);
    }

    public void onStatus(StatusEvent e) {
        game.setRobotStatus(e.getStatus());
        game.setBattlefieldHeight(this.getBattleFieldHeight());
        game.setBattlefieldWidth(this.getBattleFieldWidth());
    }

    private void updateIsOptimalPolicy(){
        if (game.getRoundNum() >= Constants.LEARNING_ROUNDS){
            this.isOptimalPolicy = true;
        } else {
            this.isOptimalPolicy = false;
        }
    }

    private void onActionEvent() {
        State state = State.updateState(game, ql);
        Action action = chooseAction(state);
        action.execute();
        this.livingReward();
        this.distanceToEnemyReward();
        // Prevents updating q-table after the end of the round.
        if (game.isAmIAlive() == false || game.getMyEnergy() == 0)
            return;
        if (!isOptimalPolicy) {
            State nextState = State.updateState(game, ql);
            ql.updateQ(state, action, nextState, rewards);
        }
        rewards.endOfCycle();
        game.resetDataAtTheEndOfCycle();
    }
}

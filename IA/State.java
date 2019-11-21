package IA;
import com.google.common.base.Objects;
import org.w3c.dom.ranges.Range;
import robocode.RobotStatus;

import java.util.*;

public class State {
//    private transient final GameStatus gameStatus;
    private final Double myEnergy;
    private final Double opponentEnergy;
    private final Double distanceToEnemy;
    private final Double angleToEnemy;
    private final Double distanceToWall;
    public static Set<State> states;

    public State(){
//        this.gameStatus = new GameStatus();
        this.distanceToEnemy = 0.0;
        this.distanceToWall = 0.0;
        this.angleToEnemy = 0.0;
        this.myEnergy = 0.0;
        this.opponentEnergy = 0.0;
    }

    public State(GameStatus gameStatus) {
//        this.gameStatus = gameStatus;
        this.distanceToEnemy = gameStatus.getDistanceToEnemy();
        this.distanceToWall = gameStatus.getDistanceToNearestWall();
        this.angleToEnemy = gameStatus.getAngleToEnemy();
        this.myEnergy = gameStatus.getMyEnergy();
        this.opponentEnergy = gameStatus.getEnemyEnergy();
    }

    public static State updateState(final GameStatus gameStatus, final QLearning ql) {




        State state = new State(gameStatus);
        boolean condition = ql.Q.containsRow(state);

        if (!condition) {
            for (Action action : Action.values()) {
                Executable actionFunc = ql.actionFunctions.get(action);
                action.setExecutableAction(actionFunc);
                ql.Q.put(state, action, Constants.INITIAL_Q);
            }
            states.add(state);
        }
        return state;
    }

    public static void addInitial() {
        states.add(new State());
    }
//
//    public GameStatus getGameStatus() {
//        // TODO return defensive copy
//        return gameStatus;
//    }

    public static void init(){
        states = new HashSet<State>();
    }

    @Override
    public boolean equals(Object other) {
        if (other == null)
            return false;
        if (other == this)
            return true;
        if (other instanceof State) {
            return (((State)other).distanceToEnemy.equals(this.distanceToEnemy) &&
                    ((State)other).distanceToWall.equals(this.distanceToWall) &&
                    ((State)other).opponentEnergy.equals(this.opponentEnergy) &&
                    ((State)other).myEnergy.equals(this.myEnergy) &&
                    ((State)other).angleToEnemy.equals(this.angleToEnemy));
        }
        return false;
    }

    @Override
    public int hashCode() {
        int a = Objects.hashCode(
                this.distanceToEnemy,
                this.distanceToWall,
                this.myEnergy,
                this.opponentEnergy,
                this.angleToEnemy);
        return a;
    }

    @Override
    public String toString() {
        return "ToEnemy: " + distanceToEnemy +
                " ToWall: " + distanceToWall +
                " AngleToEnemy: " + angleToEnemy +
                " OpponentEnergy: " + opponentEnergy +
                " MyEnergy: " + myEnergy;
    }
}

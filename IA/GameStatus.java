package IA;

import robocode.RobotStatus;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class GameStatus {
    private RobotStatus robotStatus;

    // The coordinates of the last scanned enemy robot
    private double enemyX;
    private double enemyY;
    private double previousEnemyX;
    private double previousEnemyY;

    private double enemyEnergy;
    private double previousEnemyEnergy;
    private List<Boolean> enemyShoots = new ArrayList<>();

    private double angleToEnemy;

    private double battleFieldWith;
    private double battleFieldHeight;

    // If my robot is alive
    private boolean amIAlive = true;

    public void resetDataAtTheEndOfCycle() {
        this.enemyShoots = new ArrayList<>();
    }

    public double getX() {
        return this.robotStatus.getX();
    }

    public double getY() {
        return this.robotStatus.getY();
    }

    public double getHeading() {
        return this.robotStatus.getHeading();
    }

    public int getRoundNum() {
        return robotStatus.getRoundNum();
    }

    public RobotStatus getRobotStatus() {
        return robotStatus;
    }

    public void setRobotStatus(RobotStatus robotStatus) {
        this.robotStatus = robotStatus;
    }

    public double getAngleToEnemy() {
        return this.angleToEnemy;
    }

    public void setAngleToEnemy(double angle) {
        this.angleToEnemy = angle;
    }

    public double getEnemyX() {
        return enemyX;
    }

    public void setEnemyX(double enemyX) {
        this.previousEnemyX = this.enemyX;
        this.enemyX = enemyX;
    }

    public double getEnemyY() {
        return enemyY;
    }

    public void setEnemyY(double enemyY) {
        this.previousEnemyY = this.enemyY;
        this.enemyY = enemyY;
    }

    public double getEnemyEnergy() {
        return enemyEnergy;
    }

    public void setEnemyEnergy(double enemyEnergy) {
        this.previousEnemyEnergy = this.enemyEnergy;
        this.enemyEnergy = enemyEnergy;
    }

    public double getMyEnergy() {
        return this.robotStatus.getEnergy();
    }

    public boolean isAmIAlive() {
        return amIAlive;
    }

    public void setAmIAlive(boolean amIAlive) {
        this.amIAlive = amIAlive;
    }

    public double getDistanceToNearestWall() {
        double xLeftOffset = this.robotStatus.getX();
        double xRightOffset = this.battleFieldWith - this.robotStatus.getX();

        double yTopOffset = this.battleFieldHeight - this.robotStatus.getY();
        double yBottomOffset = this.robotStatus.getY();

        double[] offsets = {xLeftOffset, xRightOffset, yBottomOffset, yTopOffset};
        Arrays.sort(offsets);

        // return smallest number
        return offsets[0];
    }

    public double getDistanceToNearestWall(double myX, double myY) {
        double xLeftOffset = myX;
        double xRightOffset = this.battleFieldWith - myX;

        double yTopOffset = this.battleFieldHeight - myY;
        double yBottomOffset = myY;

        double[] offsets = {xLeftOffset, xRightOffset, yBottomOffset, yTopOffset};
        Arrays.sort(offsets);

        // return smallest number
        return offsets[0];
    }

    public void setBattlefieldHeight(double battleFieldHeight) {
        this.battleFieldHeight = battleFieldHeight;
    }

    public void setBattlefieldWidth(double battleFieldWidth) {
        this.battleFieldWith = battleFieldWidth;
    }

    public double getBattleFieldWith() {
        return battleFieldWith;
    }

    public double getBattleFieldHeight() {
        return battleFieldHeight;
    }

    public boolean getEnemyShotABullet() {
        double minDifference = 0.1;
        double maxDifference = 3.0;

        double difference = this.previousEnemyEnergy - this.enemyEnergy;
        if (difference >= minDifference && difference <= maxDifference) {
            this.enemyShoots.add(true);
        }
        this.enemyShoots.add(false);

        if (this.enemyShoots.contains(true)) {
            return true;
        }
        return false;
    }

    public double getEnemyMovementDirection() {
        return this.angleBetween2Points(this.enemyX, this.enemyY, this.previousEnemyX, this.previousEnemyX);
    }

    public double getDistanceToEnemy() {
        double x = this.getX();
        double y = this.getY();
        double ex = this.getEnemyX();
        double ey = this.getEnemyY();
        return this.distanceBetween2Points(x,y,ex,ey);
    }

    private double distanceBetween2Points(double x1, double y1, double x2, double y2) {
        double distance = Math.sqrt( Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2) );
        return distance;
    }

    private double angleBetween2Points(double x1, double y1, double x2, double y2) {
        double dx = x2 - x1;
        double dy = y2 - y1;
        return Math.atan2(dy, dx) * 180 / Math.PI;
    }
}

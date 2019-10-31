package IA;
import org.apache.commons.math3.distribution.EnumeratedIntegerDistribution;
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
public class HAL9001 extends Robot
{
    /**
     * run: HAL9001's default behavior
     */
    private String[] states = new String[]{"gunR", "gunL", "ahead"};
    private String[][] actions = new String[][]{
            {"gunR-gunR", "gunR-gunL", "gunR-ahead"},
            {"gunL-gunR", "gunL-gunL", "gunL-ahead"},
            {"ahead-gunR", "ahead-gunL", "ahead-ahead"}};
    private double[][] probMatrix = new double[][]{
            {0.9, 0.075, 0.025},
            {0.15, 0.8, 0.05},
            {0.25, 0.25, 0.5}};

    public void onPaint(Graphics2D g) {
        // Set the paint color to red
        g.setColor(java.awt.Color.RED);
        // Paint a filled rectangle at (50,50) at size 100x150 pixels
        g.fillRect(50, 50, 100, 150);
    }


    public void run() {
        String currentState = states[0];
        String action = null;

        // Robot main loop
        System.out.println(this.probMatrix.length);
        while(true) {
            int[] indexes = new int[]{0,1,2};

            if ( currentState.equals(states[0]) ){
                EnumeratedIntegerDistribution dist = new EnumeratedIntegerDistribution(indexes, this.probMatrix[0]);
                action = actions[0][dist.sample()];
            }

            else if (currentState.equals(states[1])) {
                EnumeratedIntegerDistribution dist = new EnumeratedIntegerDistribution(indexes, this.probMatrix[1]);
                action = actions[1][dist.sample()];
            }

            else if (currentState.equals(states[2])) {

                EnumeratedIntegerDistribution dist = new EnumeratedIntegerDistribution(indexes, this.probMatrix[2]);
                action = actions[2][dist.sample()];
            }

            currentState = fAction(action);
        }
    }

    public String fAction(String action) {
        String state = "";
        if ( action.equals(this.actions[0][0]) || action.equals(this.actions[1][0]) || action.equals(this.actions[2][0]) ) {
            turnGunRight(360);
            state = this.states[0];
        }
        else if (action.equals(this.actions[0][1]) || action.equals(this.actions[1][1]) || action.equals(this.actions[2][1])) {
            turnGunLeft(360);
            state = this.states[1];
        }
        else if (action.equals(this.actions[0][2]) || action.equals(this.actions[1][2]) || action.equals(this.actions[2][2])) {
            ahead(100);
            state = this.states[2];
        }
        return state;
    };

    /**
     * onScannedRobot: What to do when you see another robot
     */
    public void onScannedRobot(ScannedRobotEvent e) {
        // Replace the next line with any behavior you would like
        fire(1);
    }

    /**
     * onHitByBullet: What to do when you're hit by a bullet
     */
    public void onHitByBullet(HitByBulletEvent e) {
        // Replace the next line with any behavior you would like
        back(10);
    }

    /**
     * onHitWall: What to do when you hit a wall
     */
    public void onHitWall(HitWallEvent e) {
        // Replace the next line with any behavior you would like
        back(20);
    }
}

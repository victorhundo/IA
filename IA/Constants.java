package IA;

public class Constants {

    public static final int MOVE_DISTANCE = 25;  // Default distance for movement actions
    public static final int TURN_ANGLE = 30;     // Default distance for turn actions
    public static final int FIRE_POWER = 1;     // Default distance for turn actions


    public static final double INITIAL_Q = 0.0;
    public static final double LEARNING_ROUNDS = 200;

    public static final double GAMMA = 0.8;
    public static final double ALPHA = 0.2;
    public static final double EPSILON = 0.05;

    public static final int HIT_A_WALL = 0;
    public static final double COLLISION_WITH_ENEMY = 0;
    public static final double COLLISION_AND_KILL_ENEMY = 0;
    public static final double LIVING_REWARD = 1;
    public static final double HIT_BY_BULLET = -1;
    public static final double DISTANCE_TO_ENEMY_LESS_THAN_50 = -10;
    public static final double DISTANCE_TO_ENEMY_LESS_THAN_200 = -1;

}

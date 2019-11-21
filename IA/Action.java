package IA;

import static IA.Constants.MOVE_DISTANCE;
import static IA.Constants.TURN_ANGLE;

public enum Action {
    AHEAD(MOVE_DISTANCE),
    BACK(MOVE_DISTANCE),
    TURN_LEFT(TURN_ANGLE),
    TURN_RIGHT(TURN_ANGLE);

    private transient Executable executableAction;
    private int value;

    Action(int value) {
        this.value = value;
    }

    public void execute() {
        this.executableAction.execute();
    }

    @Override
    public String toString() {
        return this.name();
    }

    public void setExecutableAction(Executable executableAction) {
        this.executableAction = executableAction;
    }

    public int value() {
        return this.value;
    }
}

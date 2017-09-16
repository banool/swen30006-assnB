package robotstates;

import automail.Robot;

public class RobotState {
    private State currentState;

    public RobotState(Robot robot) {
        currentState = new RobotStateReturning();
    }

    public void setState(State s) {
        currentState = s;
    }

    public void step(Robot robot) {
        currentState.step(this, robot);
    }
}

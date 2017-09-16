package robotstates;

import automail.Robot;

public interface State {
	void step(RobotState wrapper, Robot robot);
}

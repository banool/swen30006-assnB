package robotstates;

import automail.Building;
import automail.Robot;
import robotstates.RobotState;

public class RobotStateReturning implements State {
	
	@Override
	public void step(RobotState wrapper, Robot robot) {
		// TODO Auto-generated method stub
		if (robot.getCurrentFloor() == Building.MAILROOM_LOCATION) {
			wrapper.setState(new RobotStateWaiting());
		} else {
			/** If the robot is not at the mailroom floor yet, then move towards it! */
			robot.moveTowards(Building.MAILROOM_LOCATION);
			break;
		}
	}

}

package robotstates;

import automail.Building;
import automail.Robot;
import exceptions.ExcessiveDeliveryException;
import robotstates.RobotState;

public class RobotStateDelivering implements State {
	
	@Override
	public void step(RobotState wrapper, Robot robot) {
		/** Check whether or not the call to return is triggered manually **/
		if (robot.getCurrentFloor() == robot.getDestinationFloor()) { // If already here drop off item
			/** Delivery complete, report this to the simulator! */
			robot.getDelivery().deliver(robot.getDeliveryItem());
			robot.tube.pop();
			deliveryCounter++;
			if (deliveryCounter > this.behaviour.getMaxTake()) {
				throw new ExcessiveDeliveryException(this.behaviour.getMaxTake());
			}
			/** Check if want to return or if there are more items in the tube */
			if (tube.isEmpty() || behaviour.returnToMailRoom(tube)) { // No items or robot requested return
				changeState(RobotState.RETURNING);
			} else {
				/**
				 * If there are more items, set the robot's route to the location to deliver the
				 * item
				 */
				setRoute();
				changeState(RobotState.DELIVERING);
			}
	}

}

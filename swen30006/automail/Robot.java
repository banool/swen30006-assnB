package automail;

import exceptions.ExcessiveDeliveryException;
import strategies.IMailPool;
import strategies.RobotBehaviour;

/**
 * The robot delivers mail!
 */
public class Robot {

	StorageTube tube;
	RobotBehaviour behaviour;
	private IMailDelivery delivery;

	/** Possible states the robot can be in */
	public enum RobotState {
		DELIVERING, WAITING, RETURNING
	}

	public RobotState current_state;
	private int current_floor;
	private int destination_floor;
	private IMailPool mailPool;

	private MailItem deliveryItem;

	private int deliveryCounter;

	/**
	 * Initiates the robot's location at the start to be at the mailroom also set it
	 * to be waiting for mail.
	 * 
	 * @param behaviour
	 *            governs selection of mail items for delivery and behaviour on
	 *            priority arrivals
	 * @param delivery
	 *            governs the final delivery
	 * @param mailPool
	 *            is the source of mail items
	 */
	public Robot(RobotBehaviour behaviour, IMailDelivery delivery, IMailPool mailPool) {
		current_state = RobotState.RETURNING;
		setCurrentFlloor(Building.MAILROOM_LOCATION);
		this.behaviour = behaviour;
		tube = new StorageTube(behaviour.getMaxTake());
		this.setDelivery(delivery);
		this.mailPool = mailPool;
		this.deliveryCounter = 0;
	}

	/**
	 * This is called on every time step
	 * 
	 * @throws ExcessiveDeliveryException
	 *             if robot delivers more than the capacity of the tube without
	 *             refilling
	 */
	public void step() throws ExcessiveDeliveryException {

		boolean go = false;

		switch (current_state) {
		/**
		 * This state is triggered when the robot is returning to the mailroom after a
		 * delivery
		 */
		case RETURNING:
			/**
			 * If its current position is at the mailroom, then the robot should change
			 * state
			 */
			if (getCurrentFloor() == Building.MAILROOM_LOCATION) {
				changeState(RobotState.WAITING);
			} else {
				/** If the robot is not at the mailroom floor yet, then move towards it! */
				moveTowards(Building.MAILROOM_LOCATION);
				break;
			}
		case WAITING:
			/** Tell the sorter the robot is ready */
			go = behaviour.fillStorageTube(mailPool, tube);
			// System.out.println("Tube total size: "+tube.getTotalOfSizes());
			/**
			 * If the StorageTube is ready and the Robot is waiting in the mailroom then
			 * start the delivery
			 */
			if (go) {

				deliveryCounter = 0; // reset delivery counter
				setRoute();
				changeState(RobotState.DELIVERING);
			}
			break;
		case DELIVERING:
			/** Check whether or not the call to return is triggered manually **/
			if (getCurrentFloor() == getDestinationFloor()) { // If already here drop off item
				/** Delivery complete, report this to the simulator! */
				getDelivery().deliver(getDeliveryItem());
				tube.pop();
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
			} else {
				if (behaviour.returnToMailRoom(tube)) { // Robot requested return
					changeState(RobotState.RETURNING);
				} else {
					/** The robot is not at the destination yet, move towards it! */
					moveTowards(getDestinationFloor());
				}
			}
			break;
		}
	}

	/**
	 * Sets the route for the robot
	 */
	private void setRoute() {
		/** Pop the item from the StorageUnit */
		setDeliveryItem(tube.peek());
		/** Set the destination floor */
		setDestinationFloor(getDeliveryItem().getDestFloor());
	}

	/** Generic function that moves the robot towards the destination */
	public void moveTowards(int destination) {
		if (getCurrentFloor() < destination) {
			setCurrentFlloor(getCurrentFloor() + 1);
		} else {
			setCurrentFlloor(getCurrentFloor() - 1);
		}
	}

	/**
	 * Prints out the change in state
	 * 
	 * @param nextState
	 */
	private void changeState(RobotState nextState) {
		if (current_state != nextState) {
			System.out.println("T: " + Clock.Time() + " | Robot changed from " + current_state + " to " + nextState);
		}
		current_state = nextState;
		if (nextState == RobotState.DELIVERING) {
			System.out.println("T: " + Clock.Time() + " | Deliver   " + getDeliveryItem().toString());
		}
	}

	/**
	 * @return the current_floor
	 */
	public int getCurrentFloor() {
		return current_floor;
	}

	/**
	 * @param current_floor the current_floor to set
	 */
	public void setCurrentFlloor(int current_floor) {
		this.current_floor = current_floor;
	}

	/**
	 * @return the destination_floor
	 */
	public int getDestinationFloor() {
		return destination_floor;
	}

	/**
	 * @param destination_floor the destination_floor to set
	 */
	public void setDestinationFloor(int destination_floor) {
		this.destination_floor = destination_floor;
	}

	/**
	 * @return the delivery
	 */
	public IMailDelivery getDelivery() {
		return delivery;
	}

	/**
	 * @param delivery the delivery to set
	 */
	public void setDelivery(IMailDelivery delivery) {
		this.delivery = delivery;
	}

	/**
	 * @return the deliveryItem
	 */
	public MailItem getDeliveryItem() {
		return deliveryItem;
	}

	/**
	 * @param deliveryItem the deliveryItem to set
	 */
	public void setDeliveryItem(MailItem deliveryItem) {
		this.deliveryItem = deliveryItem;
	}

}

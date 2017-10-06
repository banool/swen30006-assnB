/*
 * Group number: 17
 * Daniel Porteous (696965), David Stern (585870), Hao Le (695540)
 */

package strategies;

import automail.PriorityMailItem;
import automail.StorageTube;
import exceptions.TubeFullException;

public class SimpleRobotBehaviour extends CommsRobotBehaviour {
	
	/** The maximum number of items this robot can take (tube size). */
	private static final int SIMPLE_COMMS_ROBOT_MAX_TAKE = 4;

    /**
     * Constructor that sends the size of the storage tube to the super class' constructor.
     */
	public SimpleRobotBehaviour() {
		super(SIMPLE_COMMS_ROBOT_MAX_TAKE);
	}

	/**
     * This method fills the storage tube passed into it, with mail from the mail pool passed into it, if any.
	 * @param mailPool
	 *            used to put back or get mail.
	 * @param tube
	 *            refers to the pack the robot uses to deliver mail.
	 * @return
	 */
	@Override
	public boolean fillStorageTube(IMailPool mailPool, StorageTube tube) {
		// Priority items are important;
		// if there are some, grab one and go, otherwise take as many items as we can
		// and go
		try {
			// Start afresh
			setNewPriority(0);
			while (!tube.isEmpty()) {
				mailPool.addToPool(tube.pop());
			}
			// Check for a top priority item
			if (mailPool.getPriorityPoolSize() > 0) {
				// Add priority mail item
				tube.addItem(mailPool.getHighestPriorityMail());
				// Go deliver that item
				return true;
			} else {
				// Get as many non-priority items as available or as fit
				while (tube.getSize() < getMaxTake() && mailPool.getNonPriorityPoolSize() > 0) {
					tube.addItem(mailPool.getNonPriorityMail());
				}
				return (tube.getSize() > 0);
			}
		} catch (TubeFullException e) {
			e.printStackTrace();
		}
		return false;
	}


    /**
     * This method returns true if the robot's tube is empty, telling the robot to return to the mail room if this is
     * true.
     * @param tube
     *            refers to the pack the robot uses to deliver mail.
     * @return
     */
	@Override
	public boolean returnToMailRoom(StorageTube tube) {
		// Only return if we don't have a priority item and a new one came in
		if (tube.getSize() > 0) {
			Boolean priority = (tube.peek() instanceof PriorityMailItem);
			return !priority && hasNewPriority();
		} else {
			return false;
		}
	}


}

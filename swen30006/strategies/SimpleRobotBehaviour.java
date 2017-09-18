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
	
	public SimpleRobotBehaviour() {
		super(SIMPLE_COMMS_ROBOT_MAX_TAKE);
	}

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

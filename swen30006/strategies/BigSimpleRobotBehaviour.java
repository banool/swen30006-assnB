package strategies;

import automail.Clock;
import automail.PriorityMailItem;
import automail.StorageTube;
import exceptions.TubeFullException;

public class BigSimpleRobotBehaviour extends RobotBehaviour {
	
	private static int BIG_SIMPLE_COMMS_ROBOT_MAX_TAKE = 6;

	public BigSimpleRobotBehaviour() {
		super(BIG_SIMPLE_COMMS_ROBOT_MAX_TAKE);
	}

	@Override
	public boolean fillStorageTube(IMailPool mailPool, StorageTube tube) {
		try {
			// Start afresh
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
				// Get as many nonpriority items as available or as fit
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
		// Only return if we've finished delivering all the items in teh tube
		if (tube.getSize() > 0) {
			return false;
		} else {
			return true;
		}
	}

}

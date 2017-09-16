package strategies;

import java.util.ArrayList;
import java.util.Comparator;

import automail.MailItem;
import automail.StorageTube;
import exceptions.TubeFullException;

/**
 * The behaviour of the Big Smart robot is similar to the original
 * Smart robot, though it cannot receive notifcations from the mail room.
 * As such, the decision on when to return the mail room is simpler.
 * Filling the mail tube still uses smart behaviour however.
 * @author daniel
 */
public class BigSmartRobotBehaviour extends RobotBehaviour {

	private static final int BIG_SMART_COMMS_ROBOT_MAX_TAKE = 6;

	public BigSmartRobotBehaviour() {
		super(BIG_SMART_COMMS_ROBOT_MAX_TAKE);
	}

	@Override
	public boolean returnToMailRoom(StorageTube tube) {
		// The original smart behaviour doesn't work here. Without being able to
		// receive notifications from the mail room, the bot will enter an
		// infinite loop when the bot's tube is less than half full.s
		if (!tube.isEmpty()) {
			return false;
		}
		return true;
	}


	@Override
	public boolean fillStorageTube(IMailPool mailPool, StorageTube tube) {

		ArrayList<MailItem> tempTube = new ArrayList<MailItem>();

		// Empty my tube
		while (!tube.isEmpty()) {
			mailPool.addToPool(tube.pop());
		}

		// Grab priority mail
		while (tempTube.size() < tube.MAXIMUM_CAPACITY) {
			if (containMail(mailPool, MailPool.PRIORITY_POOL)) {
				tempTube.add(mailPool.getHighestPriorityMail());
			} else {
				// Fill it up with non priority
				if (containMail(mailPool, MailPool.NON_PRIORITY_POOL)) {
					tempTube.add(mailPool.getNonPriorityMail());
				} else {
					break;
				}

			}
		}

		// Sort tempTube based on floor.
		tempTube.sort(new ArrivalComparer());

		// Iterate through the tempTube
		while (tempTube.iterator().hasNext()) {
			try {
				tube.addItem(tempTube.remove(0));
			} catch (TubeFullException e) {
				e.printStackTrace();
			}
		}

		// Check if there is anything in the tube
		if (!tube.isEmpty()) {
			return true;
		}
		return false;
	}

	private boolean containMail(IMailPool m, String mailPoolIdentifier) {
		if (mailPoolIdentifier.equals(MailPool.PRIORITY_POOL) && m.getPriorityPoolSize() > 0) {
			return true;
		} else if (mailPoolIdentifier.equals(MailPool.NON_PRIORITY_POOL) && m.getNonPriorityPoolSize() > 0) {
			return true;
		} else {
			return false;
		}
	}

	private class ArrivalComparer implements Comparator<MailItem> {
		@Override
		public int compare(MailItem m1, MailItem m2) {
			return MailPool.compareArrival(m1, m2);
		}
	}

}

/*
 * Group number: 17
 * Daniel Porteous (696965), David Stern (585870), Hao Le (695540)
 */

package strategies;

import java.util.ArrayList;
import java.util.Comparator;

import automail.MailItem;
import automail.StorageTube;
import exceptions.TubeFullException;

/**
 * The behaviour of the Big Smart robot is similar to the original
 * Smart robot, though it cannot receive notifications from the mail room.
 * As such, the decision on when to return the mail room is simpler.
 * Filling the mail tube still uses smart behaviour however.
 * @author daniel
 */
public class BigSmartRobotBehaviour extends RobotBehaviour {

	/** The maximum number of items this robot can take (tube size). */
	private static final int BIG_SMART_COMMS_ROBOT_MAX_TAKE = 6;

	/* Constructor passing the maximum capacity to its super-class */
	public BigSmartRobotBehaviour() {
		super(BIG_SMART_COMMS_ROBOT_MAX_TAKE);
	}

	/**
     * This method returns true if the robot's tube is empty, telling the robot to return to the mail room if this is
     * true.
     * @param tube
     *            refers to the StorageTube the robot uses to store mail it's delivering.
     * @return When this is true, the robot is returned to the mail room.
	 */
    @Override
	public boolean returnToMailRoom(StorageTube tube) {
		// The original smart behaviour doesn't work here. Simply return when the tube is empty.
		if (!tube.isEmpty()) {
			return false;
		}
		return true;
	}

    /**
     * This method fills the storage tube passed into it, with mail from the mail pool passed into it, if any.
     * @param mailPool
     *            used to put back or get mail.
     * @param tube
     *            refers to the pack the robot uses to deliver mail.
     * @return
     *            A boolean, indicating whether or not the tube was filled with any mail items.
     */
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

	/**
	 * Returns true if the given mail pool has items, false otherwise.
	 * @param m                     A MailPool, the pool of mail in the Mail Room (implementing the IMailPool interface)
	 * @param mailPoolIdentifier    An identifier indicating the type of pool
	 * @return boolean              True if the identified pool in the MailPool instance contains any mail, else False.
	 */
	private boolean containMail(IMailPool m, String mailPoolIdentifier) {
		if (mailPoolIdentifier.equals(MailPool.PRIORITY_POOL) && m.getPriorityPoolSize() > 0) {
			return true;
		} else if (mailPoolIdentifier.equals(MailPool.NON_PRIORITY_POOL) && m.getNonPriorityPoolSize() > 0) {
			return true;
		} else {
			return false;
		}
	}

    /**
     *  Comparator used by FillStorageTube to sort the mail tube.
     */
	private class ArrivalComparer implements Comparator<MailItem> {
		@Override
		public int compare(MailItem m1, MailItem m2) {
			return MailPool.compareArrival(m1, m2);
		}
	}

}

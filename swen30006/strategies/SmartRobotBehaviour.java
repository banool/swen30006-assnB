/*
 * Group number: 17
 * Daniel Porteous (696965), David Stern (585870), Hao Le (695540)
 */

package strategies;

import java.util.ArrayList;
import java.util.Comparator;

import automail.MailItem;
import automail.PriorityMailItem;
import automail.StorageTube;
import exceptions.TubeFullException;

public class SmartRobotBehaviour extends CommsRobotBehaviour {

	/** The maximum number of items this robot can take (tube size). */
	private static final int SMART_COMMS_ROBOT_MAX_TAKE = 4;

	public SmartRobotBehaviour() {
		super(SMART_COMMS_ROBOT_MAX_TAKE);
	}

    /**
     * This method returns false if the robot's tube is not empty, and contains more priority items than non-priority
     * items, and the tube is less than half full, otherwise returns true, indicating that the robot should return
     * to the mailroom.
     * @param tube
     *            refers to the pack the robot uses to deliver mail.
     * @return boolean
     *            False if the above described circumstances are fulfilled, otherwise false.
     */
	@Override
	public boolean returnToMailRoom(StorageTube tube) {

		// Check if my tube contains only priority items
		if (!tube.isEmpty()) {
			int priorityCount = 0;
			int nonPriorityCount = 0;

			// There has to be more priority than non-priority to keep going
			for (MailItem m : tube.getTube()) {
				if (m instanceof PriorityMailItem) {
					priorityCount++;
				} else {
					nonPriorityCount++;
				}
			}

			if (priorityCount >= nonPriorityCount) {
				return false;
			} else {
				// Check if there is more than 1 priority arrival and the tube is
				// currently at least half full.
				if (getNewPriority() > 1 && tube.getSize() >= tube.MAXIMUM_CAPACITY / 2) {
					return true;
				} else {
					return false;
				}
			}

		}
		// If the tube is empty, the robot should return to the MailPool
		else {
			return true;
		}
	}


    /**
     * * This method fills the storage tube passed into it, with mail from the mail pool passed into it, if any.
     * @param mailPool
     *            used to put back or get mail.
     * @param tube
     *            refers to the pack the robot uses to deliver mail.
     * @return boolean
     *            true if the tube was filled with any mail items, otherwise false.
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
			setNewPriority(0);
			return true;
		}
		return false;
	}

	/**
	 * A method that indicates whether or not the indicated mail pool contains any mail.
	 * @param m                     A MailPool instance implementing the IMailPool interface.
	 * @param mailPoolIdentifier    A String indicating which type mailpool the caller of the method is checking.
	 * @return  boolean             True if the indicated mailpool is not empty.
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
     * A simple comparator class used to sort the robot's storage tube.
     */
	private class ArrivalComparer implements Comparator<MailItem> {
		@Override
		public int compare(MailItem m1, MailItem m2) {
			return MailPool.compareArrival(m1, m2);
		}
	}

}

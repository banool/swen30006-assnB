package strategies;

import java.util.ArrayList;
import java.util.Comparator;

import automail.Clock;
import automail.MailItem;
import automail.PriorityMailItem;
import automail.StorageTube;
import exceptions.TubeFullException;

public class SmartRobotBehaviour extends CommsRobotBehaviour {



	public SmartRobotBehaviour(int max_take) {
		super(max_take);
	}

	@Override
	public boolean returnToMailRoom(StorageTube tube) {
		// Check if my tube contains only priority items
		if (!tube.isEmpty()) {
			int priorityCount = 0;
			int nonPriorityCount = 0;
			// There has to be more priority than non-priority to keep going
			for (MailItem m : tube.tube) {
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
				if (super.getNewPriority() > 1 && tube.getSize() >= tube.MAXIMUM_CAPACITY / 2) {
					return true;
				} else {
					return false;
				}

			}
		} else {
			return true;
		}
	}


	@Override
	public boolean fillStorageTube(IMailPool mailPool, StorageTube tube) {

		ArrayList<MailItem> tempTube = new ArrayList<MailItem>();

		// Empty my tube
		while (!tube.tube.isEmpty()) {
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

		// Sort tempTube based on floor. TODO It is sorting based on arrival time???
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
		if (!tube.tube.isEmpty()) {
			setNewPriority(0);
			return true;
		}
		return false;
	}

	// TODO This is a bit weird right? Should this be in MailPool?
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

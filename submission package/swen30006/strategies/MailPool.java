/*
 * Group number: 17
 * Daniel Porteous (696965), David Stern (585870), Hao Le (695540)
 */

package strategies;

import java.util.ArrayList;
import java.util.Comparator;

import automail.MailItem;
import automail.PriorityMailItem;

public class MailPool implements IMailPool {

	public static final String PRIORITY_POOL = "PRIORITY_POOL";
	public static final String NON_PRIORITY_POOL = "NON_PRIORITY_POOL";

	private ArrayList<MailItem> nonPriorityPool;
	private ArrayList<MailItem> priorityPool;

    /**
     * Constructor that initialises the two pools.
     */
	public MailPool() {
		nonPriorityPool = new ArrayList<MailItem>();
		priorityPool = new ArrayList<MailItem>();
	}

    /**
     * Method that gets the size of the priority pool.
     * @return The size of the priority pool.
     */
	public int getPriorityPoolSize() {
		return priorityPool.size();
	}

    /**
     * Method that gets the size of the non-priority pool.
     * @return The size of the non-priority pool.
     */
	public int getNonPriorityPoolSize() {
		return nonPriorityPool.size();
	}

    /**
     * Method that adds the MailItem provided as its argument into the appropriate position in the appropriate mail
     * pool.
     * @param mailItem  A MailItem to be added to one of the two pools.
     */
	public void addToPool(MailItem mailItem) {
		// Check whether it has a priority or not
		if (mailItem instanceof PriorityMailItem) {
			// Add to priority items
			priorityPool.add(mailItem);
			priorityPool.sort(new PriorityComparer());

		} else {
			// Add to nonpriority items
			nonPriorityPool.add(mailItem);
			nonPriorityPool.sort(new NonPriorityComparer());
		}
	}

    /**
     * A method that gets the highest ranked MailItem in the NonPriority Mail Pool.
     * @return  MailItem ranked the highest (the first Mail Item) in nonPriorityPool.
     */
	public MailItem getNonPriorityMail() {
		if (getNonPriorityPoolSize() > 0) {
			return nonPriorityPool.remove(0);
		} else {
			return null;
		}
	}

    /**
     * A method that gets the highest ranked PriorityMailItem in the Priority Mail Pool.
     * @return  MailItem ranked the highest (the first Priority Mail Item) in priorityPool.
     */
	public MailItem getHighestPriorityMail() {
		if (getPriorityPoolSize() > 0) {
			return priorityPool.remove(0);
		} else {
			return null;
		}

	}

    /**
     * A method that gets the best priority mail item, if it exists, else it gets the best non-priority mail item,
     * if it exists
     * @param FloorFrom
     *            the lowest floor in the range to consider
     * @param FloorTo
     *            the highest floor in the range to consider
     * @return    the best ranked mail item within the floor range provided.
     */
	public MailItem getBestMail(int FloorFrom, int FloorTo) {

		ArrayList<MailItem> tempPriority = new ArrayList<MailItem>();

		// Check if there are any priority mail within the range
		for (MailItem m : priorityPool) {
			if (isWithinRange(m, FloorFrom, FloorTo)) {
				tempPriority.add(m);
			}
		}

		// If there is already something in priority then return it as the best mail
		if (tempPriority.size() > 0) {
			// Since priorityPool is already sorted, that means items being added are
			// already sorted with the
			// highest priority being in the front of the arraylist

			return tempPriority.get(0);
		} else {

			ArrayList<MailItem> tempNonPriority = new ArrayList<MailItem>();
			// Try the same thing with nonPriorityPool
			for (MailItem m : nonPriorityPool) {
				if (isWithinRange(m, FloorFrom, FloorTo)) {
					tempNonPriority.add(m);
				}
			}
			if (tempNonPriority.size() > 0) {
				return tempNonPriority.get(0);
			}
		}

		return null;
	}

    /**
     * A method that returns whether or not the mail item in question is within the specified range.
     * @param m     The MailItem
     * @param FloorFrom     The lowest floor in the specified range (inclusive).
     * @param FloorTo       The highest floor in the specified range (inclusive).
     * @return              True iff the mail item is within the specified range (inclusive).
     */
	private boolean isWithinRange(MailItem m, int FloorFrom, int FloorTo) {

		if (m.getDestFloor() <= FloorTo && m.getDestFloor() >= FloorFrom) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Comparator class used to compare Priority Mail Items to sort the priorityPool.
	 *
	 */
	private class PriorityComparer implements Comparator<MailItem> {
		// Compare Priority level, if they are the same, try comparing arrival time
		public int compare(MailItem m1, MailItem m2) {
			if (((PriorityMailItem) m1).getPriorityLevel() > ((PriorityMailItem) m2).getPriorityLevel()) {
				return -1;
			} else if (((PriorityMailItem) m1).getPriorityLevel() == ((PriorityMailItem) m2).getPriorityLevel()) {
				return compareArrival(m1, m2);
			} else {
				return 1;
			}
		}
	}

    /**
     * Comparator class used to compare Mail Items to sort the nonPriorityPool.
     */
	private class NonPriorityComparer implements Comparator<MailItem> {

		// Compare arrival time
		public int compare(MailItem m1, MailItem m2) {
			return compareArrival(m1, m2);
		}
	}

    /**
     * A method that compared the arrival time of the two given mail items.
     * @param m1    The first mail item.
     * @param m2    The second mail item.
     * @return      -1 if the first mail item arrived before the second, 0 if the arrival time is equal, 1 otherwise.
     */
	public static int compareArrival(MailItem m1, MailItem m2) {
		if (m1.getArrivalTime() < m2.getArrivalTime()) {
			return -1;
		} else if (m1.getArrivalTime() == m2.getArrivalTime()) {
			return 0;
		} else {
			return 1;
		}
	}

}

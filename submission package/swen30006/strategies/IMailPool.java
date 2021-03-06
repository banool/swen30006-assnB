/*
 * Group number: 17
 * Daniel Porteous (696965), David Stern (585870), Hao Le (695540)
 */

package strategies;

import automail.MailItem;

/**
 * A MailPool is called from MailGenerator during a certain time step i.e. when
 * t = 3 if there is mail to be delivered, addToPool will be called which adds a
 * MailItem to the MailPool.
 */
public interface IMailPool {

	/**
	 * @return the number of priority items in the mail pool.
	 */
	public int getPriorityPoolSize();

	/**
	 * @return the number of non-priority items in the mail pool.
	 */
	public int getNonPriorityPoolSize();

	/**
	 * Adds an item to the mail pool
	 * 
	 * @param mailItem
	 *            the mail item being added.
	 */
	public void addToPool(MailItem mailItem);

	/**
	 * @return the earliest arrived nonPriority mail item from the mail pool
	 */
	public MailItem getNonPriorityMail();

	/**
	 * @return the earliest arrived priority mail item from those with the highest
	 *         priority from the mail pool
	 */
	public MailItem getHighestPriorityMail();

	/**
	 * This method treats nonPriority items as priority 0, i.e. lowest priority, and
	 * so can return either priority or nonPriority items.
	 * 
	 * @param FloorFrom
	 *            the lowest floor in the range to consider
	 * @param FloorTo
	 *            the highest floor in the range to consider
	 * @return a mailItem going to a floor in the specified range: this mail item is
	 *         the highest priority item which arrived first.
	 */
	public MailItem getBestMail(int FloorFrom, int FloorTo);

}

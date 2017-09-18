/*
 * Group number: 17
 * Daniel Porteous (696965), David Stern (585870), Hao Le (695540)
 */

package automail;

/**
 * a MailDelivery is used by the Robot to deliver mail once it has arrived at
 * the correct location
 */
public interface IMailDelivery {

	/**
	 * Delivers an item at its floor
	 * 
	 * @param mailItem
	 *            the mail item being delivered.
	 */
	void deliver(MailItem mailItem);

}
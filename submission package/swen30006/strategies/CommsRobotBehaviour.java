/*
 * Group number: 17
 * Daniel Porteous (696965), David Stern (585870), Hao Le (695540)
 */

package strategies;

/**
 * @author David.
 * 
 * This class adds additional behaviour on top of RobotBehaviour,
 * specifically the ability to communicate with the MailRoom. This can
 * be used to check, for example, if a priority item has arrived.
 */
public abstract class CommsRobotBehaviour extends RobotBehaviour {

	// Used if we are notified that a priority item has arrived.
	private int newPriority;

	/**
	 * Constructor that passes the size of tube along to the super constructor and sets the current number of priority
	 * items to zero.
	 * @param max_take	The size of the tube
	 */
	public CommsRobotBehaviour(int max_take) {
		super(max_take);
		this.newPriority = 0;
	}

	/**
	 * Record that a new priority item has arrived.
	 * @param priority  Priority level of the new arrival
	 */
	public void priorityArrival(int priority) {
		newPriority++;
	}

	/**
	 * Get the current value of newPriority.
	 * @return newPriority
	 */
	public int getNewPriority() {
		return newPriority;
	}

	/**
	 * Check whether there are any new priority items. 
	 * This is useful for less intelligent behaviours that only care
	 * about whether a new priority item has arrived, not how many.
	 * @return  boolean True if the newPriority attribute is not 0.
	 */
	public boolean hasNewPriority() {
		return (newPriority != 0);
	}

	/**
	 * Set the value of newPriority.
	 * @param newPriority   The new value to set this.newPriority to.
	 */
	public void setNewPriority(int newPriority) {
		this.newPriority = newPriority;
	}
}

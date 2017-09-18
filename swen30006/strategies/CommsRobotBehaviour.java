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
	 * Constructor that passes the size of tube along to the super 
	 * constructor and sets the current number of priority items to zero.
	 * @param max_take
	 */
	public CommsRobotBehaviour(int max_take) {
		super(max_take);
		this.newPriority = 0;
	}

	/**
	 * Record that a new priority item has arrived.
	 * @param priority
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
	 * @return
	 */
	public boolean hasNewPriority() {
		return (newPriority != 0);
	}

	/**
	 * Set the value of newPriority.
	 * @param newPriority
	 */
	public void setNewPriority(int newPriority) {
		this.newPriority = newPriority;
	}
}

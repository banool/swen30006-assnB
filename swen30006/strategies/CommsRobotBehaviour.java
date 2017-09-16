package strategies;

/**
 * @author David.
 * 
 *         This class adds additional behaviour on top of RobotBehaviour,
 *         specifically the ability to communicate with the MailRoom. This can
 *         be used to check, for example, if a priority item has arrived.
 */
public abstract class CommsRobotBehaviour extends RobotBehaviour {

	// Used if we are notified that a priority item has arrived.
	private int newPriority;

	public CommsRobotBehaviour(int max_take) {
		super(max_take);
		this.newPriority = 0;
	}

	public void priorityArrival(int priority) {
		// Record that a new one has arrived
		newPriority++;
	}

	public int getNewPriority() {
		return newPriority;
	}

	public boolean hasNewPriority() {
		return (newPriority != 0);
	}

	public void setNewPriority(int new_priority) {
		newPriority = new_priority;
	}
}

package strategies;

import automail.Clock;

/**
 * Created by David on 9/9/17.
 */
public abstract class CommsRobotBehaviour extends RobotBehaviour {


    private int newPriority; // Used if we are notified that a priority item has arrived.


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

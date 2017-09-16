package strategies;

import automail.StorageTube;

public abstract class RobotBehaviour {

    private final int MAX_TAKE;


    /**
     *
     */
    public RobotBehaviour(int max_take) {
        this.MAX_TAKE = max_take;
    }


    /**
     * @param tube
     *            refers to the pack the robot uses to deliver mail.
     * @return When this is true, the robot is returned to the mail room.
     */
    public abstract boolean returnToMailRoom(StorageTube tube);


    /**
     * @param mailPool
     *            used to put back or get mail.
     * @param tube
     *            refers to the pack the robot uses to deliver mail.
     * @return Return true to indicate that the robot is ready to start delivering.
     */
    public abstract boolean fillStorageTube(IMailPool mailPool, StorageTube tube);


    /**
     *
     * @return
     */
    public int getMaxTake() {
        return MAX_TAKE;
    }


}

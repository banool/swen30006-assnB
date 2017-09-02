package automail;

import java.util.UUID;

/**
 * Represents a mail item
 */
public class MailItem {
	
    /** Represents the destination floor to which the mail is intended to go */
    protected final int DESTINATION_FLOOR;
    /** The mail identifier */
    protected final String ID;
    /** The time the mail item arrived */
    protected final int ARRIVAL_TIME;

    /**
     * Constructor for a MailItem
     * @param dest_floor the destination floor intended for this mail item
     * @param arrival_time the time that the mail arrived
     */
    public MailItem(int dest_floor, int arrival_time){
        this.DESTINATION_FLOOR = dest_floor;
        this.ID = UUID.randomUUID().toString();
        this.ARRIVAL_TIME = arrival_time;
    }

    @Override
    public String toString(){
        return "Mail Item: " +
                "| ID: " + ID +
                "| Destination: "+ DESTINATION_FLOOR +
                "| Arrival: "+ ARRIVAL_TIME
                ;
    }

    /**
     *
     * @return the destination floor of the mail item
     */
    public int getDestFloor() {
        return DESTINATION_FLOOR;
    }
    
    /**
     *
     * @return the ID of the mail item
     */
    public String getId() {
        return ID;
    }

    /**
     *
     * @return the arrival time of the mail item
     */
    public int getArrivalTime(){
        return ARRIVAL_TIME;
    }

}

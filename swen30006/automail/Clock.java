/*
 * Group number: 17
 * Daniel Porteous (696965), David Stern (585870), Hao Le (695540)
 */

package automail;

public class Clock {

	/** Represents the current time **/
	private static int Time = 0;

	/* The threshold for the latest time for mail to arrive */
	public static final int LAST_DELIVERY_TIME =
	  Integer.parseInt(Simulation.properties.getProperty("Last_Delivery_Time"));

	/* The current time */
	public static int Time() {
		return Time;
	}

	/* Increases the current time by 1 (simulates 1 time period passing) */
	public static void Tick() {
		Time++;
	}
}

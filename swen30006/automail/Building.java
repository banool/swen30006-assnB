/*
 * Group number: 17
 * Daniel Porteous (696965), David Stern (585870), Hao Le (695540)
 */

package automail;

public class Building {

	/** The number of floors in the building **/
	public static final int FLOORS =
	  Integer.parseInt(Simulation.properties.getProperty("Number_of_Floors"));

	/** Represents the ground floor location */
	public static final int LOWEST_FLOOR =
	  Integer.parseInt(Simulation.properties.getProperty("Lowest_Floor"));

	/** Represents the mailroom location */
	public static final int MAILROOM_LOCATION = 
	  Integer.parseInt(Simulation.properties.getProperty("Location_of_MailRoom"));

}

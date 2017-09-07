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

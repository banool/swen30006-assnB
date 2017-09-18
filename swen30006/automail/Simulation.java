package automail;

import exceptions.ExcessiveDeliveryException;
import exceptions.InvalidRobotBehaviourException;
import exceptions.MailAlreadyDeliveredException;
import strategies.Automail;
import strategies.CommsRobotBehaviour;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;

/**
 * This class simulates the behaviour of AutoMail
 */
public class Simulation {

	/** Constant for the mail generator */
	private static double DELIVERY_PENALTY;

	/** The file from which we read the properties for this simulation. */
	public static final String DEFAULT_PROPERTIES = "automail.Properties";

	private static ArrayList<MailItem> MAIL_DELIVERED;
	private static double total_score = 0;

	/** Public so other classes can read the required values from it. */
	public static Properties properties;

	public static void main(String[] args) {
		
		// Read in properties, catching appropriate exceptions.
		properties = new Properties();
		try {
			readProperties();
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("There was an error reading in the properties.");
			System.exit(1);
		}
		
		DELIVERY_PENALTY = 
		  Double.parseDouble(properties.getProperty("Delivery_Penalty"));
		
		MAIL_DELIVERED = new ArrayList<MailItem>();

		/** Used to see whether a seed is initialized or not */
		HashMap<Boolean, Integer> seedMap = new HashMap<>();

		/** Read the seed from the properties if it exists */
		String seedString = properties.getProperty("Seed");
		if (seedString != null) {
			int seed = Integer.parseInt(seedString);
			seedMap.put(true, seed);
		} else {
			seedMap.put(false, 0);
		}
		Automail automail;
		try {
			automail = new Automail(new ReportDelivery());
		} catch (InvalidRobotBehaviourException e) {
			e.printStackTrace();
			return;
		}
		MailGenerator generator = new MailGenerator(automail.mailPool, seedMap);

		/** Initiate all the mail */
		generator.generateAllMail();
		int priority;
		while (MAIL_DELIVERED.size() != generator.MAIL_TO_CREATE) {
			// System.out.println("-- Step: "+Clock.Time());
			priority = generator.step();
			if (priority > 0) {
                System.out.println("T: " + Clock.Time() + " | Priority arrived");
                if (automail.robot.behaviour instanceof CommsRobotBehaviour) {
                    CommsRobotBehaviour comms_behaviour = (CommsRobotBehaviour) automail.robot.behaviour;
                    comms_behaviour.priorityArrival(priority);
                }
            }
			try {
				automail.robot.step();
			} catch (ExcessiveDeliveryException e) {
				e.printStackTrace();
				System.out.println("Simulation unable to complete..");
				System.exit(0);
			}
			Clock.Tick();
		}
		printResults();
	}

	static class ReportDelivery implements IMailDelivery {

		/** Confirm the delivery and calculate the total score */
		public void deliver(MailItem deliveryItem) {
			if (!MAIL_DELIVERED.contains(deliveryItem)) {
				System.out.println("T: " + Clock.Time() + " | Delivered " + deliveryItem.toString());
				MAIL_DELIVERED.add(deliveryItem);
				// Calculate delivery score
				total_score += calculateDeliveryScore(deliveryItem);
			} else {
				try {
					throw new MailAlreadyDeliveredException();
				} catch (MailAlreadyDeliveredException e) {
					e.printStackTrace();
				}
			}
		}

	}

	/**
	 * Reads the properties file using the .load() method of the
	 * previously instantiated properties object.
	 * @throws IOException
	 */
	private static void readProperties() throws IOException {
		FileReader inStream = null;
		try {
			inStream = new FileReader(DEFAULT_PROPERTIES);
			properties.load(inStream);
		} finally {
			if (inStream != null) {
				inStream.close();
			}
		}
	}

	private static double calculateDeliveryScore(MailItem deliveryItem) {
		// Penalty for longer delivery times
		double priority_weight = 0;
		// Take (delivery time - arrivalTime)**penalty * (1+sqrt(priority_weight))
		if (deliveryItem instanceof PriorityMailItem) {
			priority_weight = ((PriorityMailItem) deliveryItem).getPriorityLevel();
		}
		return Math.pow(Clock.Time() - deliveryItem.getArrivalTime(), DELIVERY_PENALTY) * (1 + Math.sqrt(priority_weight));
	}

	public static void printResults() {
		System.out.println("T: " + Clock.Time() + " | Simulation complete!");
		System.out.println("Final Delivery time: " + Clock.Time());
		System.out.printf("Final Score: %.2f%n", total_score);
	}
}

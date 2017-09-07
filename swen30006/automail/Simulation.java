package automail;

import exceptions.ExcessiveDeliveryException;
import exceptions.MailAlreadyDeliveredException;
import strategies.Automail;

import java.io.FileNotFoundException;
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
	// TODO move this into MailGenerator.
	private static int MAIL_TO_CREATE;
	private static double DELIVERY_PENALTY;

	// TODO comment this
	public static final String DEFAULT_PROPERTIES = "automail.Properties";

	private static ArrayList<MailItem> MAIL_DELIVERED;
	private static double total_score = 0;

	// TODO comment this.
	public static Properties properties;

	public static void main(String[] args) {
		
		// TODO comment. Reading in properties. TODO look into defaults.
		properties = new Properties();
		try {
			readProperties();
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("There was an error in reading in the properties");
			System.exit(1);
		}
		
		// TODO Move this to MailGenerator.
		// Change the MailGenerator constructor to not take this as an argument.
		MAIL_TO_CREATE =  
		  Integer.parseInt(Simulation.properties.getProperty("Mail_to_Create"));
		DELIVERY_PENALTY = 
		  Double.parseDouble(Simulation.properties.getProperty("Delivery_Penalty"));
		
		MAIL_DELIVERED = new ArrayList<MailItem>();

		/** Used to see whether a seed is initialized or not */
		HashMap<Boolean, Integer> seedMap = new HashMap<>();

		/** Read the first argument and save it as a seed if it exists */
		// TODO change this to read the seed from automail.Properties maybe?
		if (args.length != 0) {
			int seed = Integer.parseInt(args[0]);
			seedMap.put(true, seed);
		} else {
			seedMap.put(false, 0);
		}
		Automail automail = new Automail(new ReportDelivery());
		// TODO change this constructor.
		MailGenerator generator = new MailGenerator(MAIL_TO_CREATE, automail.mailPool, seedMap);

		/** Initiate all the mail */
		generator.generateAllMail();
		int priority;
		while (MAIL_DELIVERED.size() != generator.MAIL_TO_CREATE) {
			// System.out.println("-- Step: "+Clock.Time());
			priority = generator.step();
			if (priority > 0)
				automail.robot.behaviour.priorityArrival(priority);
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

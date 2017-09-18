/*
 * Group number: 17
 * Daniel Porteous (696965), David Stern (585870), Hao Le (695540)
 */

package automail;

import java.util.*;

import strategies.IMailPool;

/**
 * This class generates the mail
 */
public class MailGenerator {

	private final int MAIL_TO_CREATE_BASE = 
	  Integer.parseInt(Simulation.properties.getProperty("Mail_to_Create"));
	
	public final int MAIL_TO_CREATE;
	
	private final int PRIORITY_MAIL_RARITY =
	  Integer.parseInt(Simulation.properties.getProperty("Priority_Mail_is_One_in"));
	// We convert the percentage value into a decimal representation.
	private final double MAIL_COUNT_VARIATION =
	  Double.parseDouble(Simulation.properties.getProperty("Mail_Count_Percentage_Variation")) / 100;

	private int mailCreated;

	private final Random RANDOM;
	/** This seed is used to make the behaviour deterministic */

	private boolean complete;
	private IMailPool mailPool;

	private HashMap<Integer, ArrayList<MailItem>> allMail;

	/**
	 * Constructor for mail generation
	 * 
	 * @param mailToCreate
	 *            roughly how many mail items to create
	 * @param mailPool
	 *            where mail items go on arrival
	 * @param seed
	 *            random seed for generating mail
	 */
	public MailGenerator(IMailPool mailPool, HashMap<Boolean, Integer> seed) {
		if (seed.containsKey(true)) {
			this.random = new Random((long) seed.get(true));
		} else {
			this.random = new Random();
		}
		// Vary arriving mail by MAIL_COUNT_VARIATION.
		int variationCap = (int)(MAIL_TO_CREATE_BASE * MAIL_COUNT_VARIATION * 2);  // TODO check if this is correct
		MAIL_TO_CREATE = MAIL_TO_CREATE_BASE * 4 / 5 + random.nextInt(variationCap);
		// System.out.println("Num Mail Items: "+MAIL_TO_CREATE);
		mailCreated = 0;
		complete = false;
		allMail = new HashMap<Integer, ArrayList<MailItem>>();
		this.mailPool = mailPool;
	}

	/**
	 * @return a new mail item that needs to be delivered
	 */
	private MailItem generateMail() {
		int dest_floor = generateDestinationFloor();
		int priority_level = generatePriorityLevel();
		int arrival_time = generateArrivalTime();
		// Check if arrival time has a priority mail
		if ((random.nextInt(PRIORITY_MAIL_RARITY) > 0) || // Skew towards non priority mail
				(allMail.containsKey(arrival_time)
						&& allMail.get(arrival_time).stream().anyMatch(e -> PriorityMailItem.class.isInstance(e)))) {
			return new MailItem(dest_floor, arrival_time);
		} else {
			return new PriorityMailItem(dest_floor, priority_level, arrival_time);
		}
	}

	/**
	 * @return a destination floor between the ranges of GROUND_FLOOR to FLOOR
	 */
	private int generateDestinationFloor() {
		return Building.LOWEST_FLOOR + random.nextInt(Building.FLOORS);
	}

	/**
	 * @return a random priority level selected from 1 - 100
	 */
	private int generatePriorityLevel() {
		return 1 + random.nextInt(100);
	}

	/**
	 * @return a random arrival time before the last delivery time
	 */
	private int generateArrivalTime() {
		return 1 + random.nextInt(Clock.LAST_DELIVERY_TIME);
	}

	/**
	 * Returns a random element from an array
	 * 
	 * @param array
	 *            of objects
	 */
	private Object getRandom(Object[] array) {
		return array[random.nextInt(array.length)];
	}

	/**
	 * This class initializes all mail and sets their corresponding values,
	 */
	public void generateAllMail() {
		while (!complete) {
			MailItem newMail = generateMail();
			int timeToDeliver = newMail.getArrivalTime();
			/** Check if key exists for this time **/
			if (allMail.containsKey(timeToDeliver)) {
				/** Add to existing array */
				allMail.get(timeToDeliver).add(newMail);
			} else {
				/**
				 * If the key doesn't exist then set a new key along with the array of MailItems
				 * to add during that time step.
				 */
				ArrayList<MailItem> newMailList = new ArrayList<MailItem>();
				newMailList.add(newMail);
				allMail.put(timeToDeliver, newMailList);
			}
			/** Mark the mail as created */
			mailCreated++;

			/** Once we have satisfied the amount of mail to create, we're done! */
			if (mailCreated == MAIL_TO_CREATE) {
				complete = true;
			}
		}

	}

	/**
	 * While there are steps left, create a new mail item to deliver
	 * 
	 * @return Priority, used to notify Robot
	 */
	public int step() {
		int priority = 0;
		// Check if there are any mail to create
		if (this.allMail.containsKey(Clock.Time())) {
			for (MailItem mailItem : allMail.get(Clock.Time())) {
				mailPool.addToPool(mailItem);
				if (mailItem instanceof PriorityMailItem)
					priority = ((PriorityMailItem) mailItem).getPriorityLevel();
				System.out.println("T: " + Clock.Time() + " | Arrive    " + mailItem.toString());
			}
		}
		return priority;
	}

}

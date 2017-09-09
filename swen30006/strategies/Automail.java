package strategies;

import automail.IMailDelivery;
import automail.Robot;

public class Automail {

	public Robot robot;
	public IMailPool mailPool;

	public Automail(IMailDelivery delivery) {
		// Swap between simple provided strategies and your strategies here

		/** Initialize the MailPool */
		mailPool = new MailPool();

		/** Initialize the RobotAction */
		RobotBehaviour robotBehaviour = new SimpleRobotBehaviour();

		/** Initialize robot */
		robot = new Robot(robotBehaviour, delivery, mailPool);

	}

}

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
		RobotBehaviour robotBehaviour = new SimpleRobotBehaviour(4);
		// RobotBehaviour robotBehaviour = new SmartRobotBehaviour(4);

		/** Initialize robot */
		robot = new Robot(robotBehaviour, delivery, mailPool);

	}

}

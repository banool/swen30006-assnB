package strategies;

import automail.IMailDelivery;
import automail.Robot;
import automail.Simulation;

public class Automail {

	public Robot robot;
	public IMailPool mailPool;

	public Automail(IMailDelivery delivery) {
		// Swap between simple provided strategies and your strategies here

		/** Initialize the MailPool */
		mailPool = new MailPool();

		/** Initialize the RobotAction */
		String robotType = Simulation.properties.getProperty("Robot_Type");
		RobotBehaviour robotBehaviour;

		switch (robotType) {
			case "Small_Comms_Simple":
				robotBehaviour = new SimpleRobotBehaviour();
				break;
			case "Small_Comms_Smart":
				robotBehaviour = new SimpleRobotBehaviour();
				break;
			case "Big_Simple":
				robotBehaviour = new BigSimpleRobotBehaviour();
				break;
			default:
				// TODO: Fix this up - add exeption
				return;
		}

		/** Initialize robot */
		robot = new Robot(robotBehaviour, delivery, mailPool);

	}

}

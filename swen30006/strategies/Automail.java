package strategies;

import automail.IMailDelivery;
import automail.Robot;
import automail.Simulation;
import exceptions.InvalidRobotBehaviourException;

public class Automail {

	public Robot robot;
	public IMailPool mailPool;

	public Automail(IMailDelivery delivery) throws InvalidRobotBehaviourException {
		// Swap between simple provided strategies and your strategies here.

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
			case "Big_Smart":
				robotBehaviour = new BigSmartRobotBehaviour();
				break;
			default:
				throw new InvalidRobotBehaviourException(robotType);
		}

		/** Initialize robot */
		robot = new Robot(robotBehaviour, delivery, mailPool);

	}

}

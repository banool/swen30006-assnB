/*
 * Group number: 17
 * Daniel Porteous (696965), David Stern (585870), Hao Le (695540)
 */

package strategies;

import automail.IMailDelivery;
import automail.Robot;
import automail.Simulation;
import exceptions.InvalidRobotBehaviourException;

public class Automail {

	public Robot robot;
	public IMailPool mailPool;

	/**
	 * Constructor, throws an InvalidRobotBehaviour exception, implementing the Strategy pattern, initialising a robot
     * based on the configuration file.
	 * @param delivery
     *                  A class used by the Robot constructor to report when the robot delivers the mail items.
	 * @throws InvalidRobotBehaviourException
     *                  An exception class used to throw an exception when the specified robot behaviour is not defined.
	 */
	public Automail(IMailDelivery delivery) throws InvalidRobotBehaviourException {
		// Swap between simple provided strategies and your strategies here.

		/* Initialize the MailPool */
		mailPool = new MailPool();

		/* Initialize the RobotAction */
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

		/* Initialize robot */
		robot = new Robot(robotBehaviour, delivery, mailPool);

	}

}

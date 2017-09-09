package exceptions;

/**
 * An exception thrown when the properties file specifies a robot type that
 * does not exist.
 */
public class InvalidRobotBehaviourException extends Throwable {
	public InvalidRobotBehaviourException(String robotString) {
		super(robotString + " is not a valid robot behaviour type.");
	}
}

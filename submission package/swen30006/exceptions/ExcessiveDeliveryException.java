package exceptions;

/**
 * An exception thrown when the robot tries to deliver more items than its tube
 * capacity without refilling.
 */
public class ExcessiveDeliveryException extends Throwable {
	public ExcessiveDeliveryException(int max_take) {
		super("Attempting to deliver more than " + max_take + " items in a single trip!!");
	}
}

package Giuseppe.DigitalDelights.exception;

@SuppressWarnings("serial")
public class UnauthorizedException extends RuntimeException {
	public UnauthorizedException(String message) {
		super(message);
	}
}

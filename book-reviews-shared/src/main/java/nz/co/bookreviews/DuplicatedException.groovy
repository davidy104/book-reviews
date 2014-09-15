package nz.co.bookreviews


public class DuplicatedException extends Exception {

	public DuplicatedException() {
		super()
	}

	public DuplicatedException(String message, Throwable cause) {
		super(message, cause)
	}

	public DuplicatedException(String message) {
		super(message)
	}

	public DuplicatedException(Throwable cause) {
		super(cause)
	}
}

package nz.co.bookreviews


class NotFoundException extends Exception {

	public NotFoundException() {
		super()
	}


	public NotFoundException(String message, Throwable cause) {
		super(message, cause)
	}

	public NotFoundException(String message) {
		super(message)
	}
}

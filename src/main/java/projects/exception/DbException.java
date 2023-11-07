// import projects.exception package
package projects.exception;

// create public class DbException that extends RuntimeException. Runtime Exceptions are not explicitly declared in a method's
// signature.
// with runtime exceptions, the exception is unknown to the dev. Therefore, we do not specify the exception, we just take in the
// message or cause that is thrown and tweak the output to uniformly display the error depending on the values available.
public class DbException extends RuntimeException {
	// DbException constructor for message. RE will give the message.
	public DbException(String message) {
		// the message is from the super class of this class, RE.
		super(message);
	}

	// This will take the cause from the super class and
	public DbException(Throwable cause) {
		super(cause);
	}

	// DbException constructor for both in one.
	public DbException(String message, Throwable cause) {
		super(message, cause);
	}
}
// the purpose of having multiple constructors is to allow different sets of initial values when the DbE occurs.
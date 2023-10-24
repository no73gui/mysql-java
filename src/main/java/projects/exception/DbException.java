// import projects.exception package
package projects.exception;

// create public class DbException that extends RuntimeException
public class DbException extends RuntimeException {
	// DbException constructor for message
    public DbException(String message) {
        super(message);
    }
    // DbException constructor for cause
    public DbException(Throwable cause) {
        super(cause);
    }

    // DbException constructor for both in one.
    public DbException(String message, Throwable cause) {
        super(message, cause);
    }
}

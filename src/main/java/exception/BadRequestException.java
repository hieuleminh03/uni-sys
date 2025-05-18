package exception;

/**
 * Exception thrown when a request is invalid due to validation errors or other issues
 */
public class BadRequestException extends RuntimeException {
    
    public BadRequestException(String message) {
        super(message);
    }
    
    public BadRequestException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public BadRequestException(String field, String problem) {
        super(String.format("Invalid value for field '%s': %s", field, problem));
    }
}

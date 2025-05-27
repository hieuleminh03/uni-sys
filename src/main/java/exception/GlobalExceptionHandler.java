package exception;

import dto.response.BaseResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DataAccessException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Global exception handler for the application
 * All exceptions are caught here and converted to a standardized response
 * All HTTP responses are returned with 200 OK status but contain appropriate error codes in the response body
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    // ====== Authentication/Authorization Exceptions ======

    @ExceptionHandler(exception.AuthenticationException.class)
    public ResponseEntity<BaseResponse<Object>> handleCustomAuthenticationException(exception.AuthenticationException ex, WebRequest request) {
        log.error("Authentication failed: {}", ex.getMessage());
        return ResponseEntity.ok(BaseResponse.unauthorized(ex.getMessage()));
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<BaseResponse<Object>> handleSpringAuthenticationException(AuthenticationException ex, WebRequest request) {
        log.error("Authentication failed: {}", ex.getMessage());
        return ResponseEntity.ok(BaseResponse.unauthorized("Authentication failed"));
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<BaseResponse<Object>> handleAccessDeniedException(AccessDeniedException ex, WebRequest request) {
        log.error("Access denied: {}", ex.getMessage());
        return ResponseEntity.ok(
                BaseResponse.error(HttpStatus.FORBIDDEN.value(), "Access denied", ex, null));
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<BaseResponse<Object>> handleBadCredentialsException(BadCredentialsException ex, WebRequest request) {
        log.error("Bad credentials: {}", ex.getMessage());
        return ResponseEntity.ok(BaseResponse.unauthorized("Invalid username or password"));
    }

    // ====== Validation Exceptions ======

    @ExceptionHandler(ConflictTimeException.class)
    public ResponseEntity<BaseResponse<Object>> handleConflictTimeException(ConflictTimeException ex, WebRequest request) {
        log.error("Illegal argument: {}", ex.getMessage());
        return ResponseEntity.ok(BaseResponse.badRequest("Conflict time", ex, ex.getMessage()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<BaseResponse<Object>> handleIllegalArgumentException(IllegalArgumentException ex, WebRequest request) {
        log.error("Illegal argument: {}", ex.getMessage());
        return ResponseEntity.ok(BaseResponse.badRequest("Invalid argument provided", ex, ex.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<BaseResponse<Object>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        log.error("Validation error: {}", errors);
        return ResponseEntity.ok(BaseResponse.badRequest("Validation failed", ex, errors.toString()));
    }

    @ExceptionHandler(BindException.class)
    public ResponseEntity<BaseResponse<Object>> handleBindExceptions(BindException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        log.error("Binding error: {}", errors);
        return ResponseEntity.ok(BaseResponse.badRequest("Invalid request parameters", ex, errors.toString()));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<BaseResponse<Object>> handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
        log.error("Message not readable: {}", ex.getMessage());
        return ResponseEntity.ok(BaseResponse.badRequest("Invalid request format", ex, 
                "The request body couldn't be parsed. Please check your JSON syntax."));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<BaseResponse<Object>> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex) {
        String error = String.format("Parameter '%s' should be of type '%s'", 
                ex.getName(), Objects.requireNonNull(ex.getRequiredType()).getSimpleName());
        log.error("Type mismatch: {}", error);
        return ResponseEntity.ok(BaseResponse.badRequest("Invalid parameter type", ex, error));
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<BaseResponse<Object>> handleMissingServletRequestParameter(MissingServletRequestParameterException ex) {
        String error = String.format("Required parameter '%s' of type '%s' is missing", 
                ex.getParameterName(), ex.getParameterType());
        log.error("Missing parameter: {}", error);
        return ResponseEntity.ok(BaseResponse.badRequest("Missing parameter", ex, error));
    }

    // ====== Business Logic Exceptions ======

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<BaseResponse<Object>> handleResourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {
        log.error("Resource not found: {}", ex.getMessage());
        return ResponseEntity.ok(
                BaseResponse.error(HttpStatus.NOT_FOUND.value(), ex.getMessage(), ex, null));
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<BaseResponse<Object>> handleBadRequestException(BadRequestException ex, WebRequest request) {
        log.error("Bad request: {}", ex.getMessage());
        return ResponseEntity.ok(BaseResponse.badRequest(ex.getMessage(), ex, null));
    }

    // ====== Database Exceptions ======

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<BaseResponse<Object>> handleDataIntegrityViolationException(DataIntegrityViolationException ex, WebRequest request) {
        log.error("Data integrity violation: {}", ex.getMessage());
        
        // Extract more specific information from the exception message if possible
        String userFriendlyMessage = "Could not perform the operation due to data integrity constraints";
        
        // Check for common constraint violations
        String exMessage = ex.getMessage().toLowerCase();
        if (exMessage.contains("unique") || exMessage.contains("duplicate")) {
            userFriendlyMessage = "This record already exists or violates a uniqueness constraint";
        } else if (exMessage.contains("foreign key")) {
            userFriendlyMessage = "This operation violates a relationship constraint with another record";
        }
        
        return ResponseEntity.ok(
                BaseResponse.error(HttpStatus.CONFLICT.value(), "Database constraint violation", ex, userFriendlyMessage));
    }

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<BaseResponse<Object>> handleDataAccessException(DataAccessException ex, WebRequest request) {
        log.error("Database error: {}", ex.getMessage());
        return ResponseEntity.ok(
                BaseResponse.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Database error occurred", ex, 
                        "There was a problem accessing the database"));
    }

    // ====== Fallback Exception Handler ======

    @ExceptionHandler(Exception.class)
    public ResponseEntity<BaseResponse<Object>> handleAllUncaughtException(Exception ex, WebRequest request) {
        log.error("Unexpected error occurred", ex);
        String errorId = generateErrorId();
        log.error("Error ID: {} - Exception: {}", errorId, ex.getMessage(), ex);
        
        return ResponseEntity.ok(
                BaseResponse.error(
                    HttpStatus.INTERNAL_SERVER_ERROR.value(), 
                    "An unexpected error occurred",
                    ex, 
                    "An internal server error occurred. Please contact support with error ID: " + errorId
                )
        );
    }
    
    /**
     * Generates a unique error ID to help with troubleshooting
     */
    private String generateErrorId() {
        return "ERR-" + System.currentTimeMillis() + "-" + Math.abs(java.util.UUID.randomUUID().hashCode() % 10000);
    }
}

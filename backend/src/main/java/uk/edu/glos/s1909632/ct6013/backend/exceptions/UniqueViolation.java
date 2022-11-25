package uk.edu.glos.s1909632.ct6013.backend.exceptions;

public class UniqueViolation extends FieldError {
    private static final String title = "Unique Violation";

    /***
     * Unique Constraint Violation
     * @param propertyName Property that violated constraint
     */
    public UniqueViolation(String propertyName, String description) {
        super(propertyName, description, title);
    }

    /***
     * Unique Constraint Violation
     * @param cause Cause of
     * @param propertyName Property that violated constraint
     */
    public UniqueViolation(Throwable cause, String propertyName, String description) {
        super(cause, propertyName, description, title);
    }

    public UniqueViolation(String message, Throwable cause, boolean enableSuppression,
                           boolean writableStackTrace, String propertyName,
                           String description) {
        super(message, cause, enableSuppression, writableStackTrace, propertyName,
              description, title);
    }
}

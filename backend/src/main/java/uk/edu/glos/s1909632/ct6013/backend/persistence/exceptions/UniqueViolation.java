package uk.edu.glos.s1909632.ct6013.backend.persistence.exceptions;

public class UniqueViolation extends Exception {
    public String getPropertyName() {
        return propertyName;
    }

    private final String propertyName;

    /***
     * Unique Constraint Violation
     * @param propertyName Property that violated constraint
     */
    public UniqueViolation(String propertyName) {
        super("Unique Violation");
        this.propertyName = propertyName;
    }

    /***
     * Unique Constraint Violation
     * @param cause Cause of
     * @param propertyName Property that violated constraint
     */
    public UniqueViolation(Throwable cause, String propertyName) {
        super("Unique Violation", cause);
        this.propertyName = propertyName;
    }

    public UniqueViolation(Throwable cause, boolean enableSuppression,
                           boolean writableStackTrace, String propertyName) {
        super("Unique Violation", cause, enableSuppression, writableStackTrace);
        this.propertyName = propertyName;
    }
}

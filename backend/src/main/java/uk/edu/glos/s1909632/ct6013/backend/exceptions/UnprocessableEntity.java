package uk.edu.glos.s1909632.ct6013.backend.exceptions;

public class UnprocessableEntity extends FieldError {
    private static final String title = "Unprocessable Entity";

    public UnprocessableEntity(String propertyName, String description) {
        super(propertyName, description, title);
    }

    public UnprocessableEntity(String message, String propertyName, String description) {
        super(message, propertyName, description, title);
    }

    public UnprocessableEntity(String message, Throwable cause, String propertyName, String description) {
        super(message, cause, propertyName, description, title);
    }

    public UnprocessableEntity(Throwable cause, String propertyName, String description) {
        super(cause, propertyName, description, title);
    }

    public UnprocessableEntity(String message, Throwable cause, boolean enableSuppression,
                               boolean writableStackTrace, String propertyName, String description) {
        super(message, cause, enableSuppression, writableStackTrace, propertyName,
              description, title);
    }
}

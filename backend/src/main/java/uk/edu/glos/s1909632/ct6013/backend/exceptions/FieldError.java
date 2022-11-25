package uk.edu.glos.s1909632.ct6013.backend.exceptions;

public class FieldError extends Exception {
    private final String propertyName;
    private final String description;
    private final String title;

    public FieldError(String propertyName, String description, String title) {
        this.propertyName = propertyName;
        this.description = description;
        this.title = title;
    }

    public FieldError(String message, String propertyName, String description,
                      String title) {
        super(message);
        this.propertyName = propertyName;
        this.description = description;
        this.title = title;
    }

    public FieldError(String message, Throwable cause, String propertyName,
                      String description, String title) {
        super(message, cause);
        this.propertyName = propertyName;
        this.description = description;
        this.title = title;
    }

    public FieldError(Throwable cause, String propertyName, String description,
                      String title) {
        super(cause);
        this.propertyName = propertyName;
        this.description = description;
        this.title = title;
    }

    public FieldError(String message, Throwable cause, boolean enableSuppression,
                      boolean writableStackTrace, String propertyName, String description,
                      String title) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.propertyName = propertyName;
        this.description = description;
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public String getTitle() {
        return title;
    }

    public String getPropertyName() {
        return propertyName;
    }
}

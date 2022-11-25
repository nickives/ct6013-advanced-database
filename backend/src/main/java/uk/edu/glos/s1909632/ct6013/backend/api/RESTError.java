package uk.edu.glos.s1909632.ct6013.backend.api;

/***
 * RESTError - RFC7807 compliant REST problem details object.
 */
public final class RESTError {
    public final String type;
    public final String title;
    public final Number status;
    public final String detail;
    public final String field;

    RESTError(String type, String title, Number status, String detail, String field) {
        this.type = type;
        this.title = title;
        this.status = status;
        this.detail = detail;
        this.field = field;
    }
}

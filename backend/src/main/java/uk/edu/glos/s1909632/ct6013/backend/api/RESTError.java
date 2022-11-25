package uk.edu.glos.s1909632.ct6013.backend.api;

public final class RESTError {
    public final String title;
    public final Number status;
    public final String detail;

    RESTError(String title, Number status, String detail) {
        this.title = title;
        this.status = status;
        this.detail = detail;
    }
}

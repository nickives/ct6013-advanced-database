package uk.edu.glos.s1909632.ct6013.backend;

public enum Grade {
    FIRST("First"),
    TWO_ONE("2:1"),
    TWO_TWO("2:2"),
    THIRD("Third"),
    FAIL("Fail");

    private final String text;

    Grade(final String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }
}

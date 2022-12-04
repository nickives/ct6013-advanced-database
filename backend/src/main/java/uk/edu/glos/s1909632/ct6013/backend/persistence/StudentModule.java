package uk.edu.glos.s1909632.ct6013.backend.persistence;

public interface StudentModule {
    Module getModule();
    void setModule(Module module);
    Student getStudent();
    Long getMark();
    StudentModule setMark(Long mark);
}

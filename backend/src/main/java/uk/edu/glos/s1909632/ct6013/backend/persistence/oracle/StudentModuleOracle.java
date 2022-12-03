package uk.edu.glos.s1909632.ct6013.backend.persistence.oracle;

import jakarta.persistence.EntityManager;
import uk.edu.glos.s1909632.ct6013.backend.persistence.Module;
import uk.edu.glos.s1909632.ct6013.backend.persistence.Student;
import uk.edu.glos.s1909632.ct6013.backend.persistence.StudentModule;
import uk.edu.glos.s1909632.ct6013.backend.persistence.oracle.ents.StudentModuleEntity;

public class StudentModuleOracle implements StudentModule {
    private final StudentModuleEntity studentModule;
    private final EntityManager em;

    public StudentModuleOracle(StudentModuleEntity studentModule, EntityManager em) {
        this.studentModule = studentModule;
        this.em = em;
    }

    @Override
    public Module getModule() {
        return new ModuleOracle(studentModule.getModule(), em);
    }

    @Override
    public void setModule(Module module) {
        try {
            ModuleOracle moduleOracle = (ModuleOracle) module;
            studentModule.setModule(moduleOracle.getEntity());
        } catch (ClassCastException e) {
            throw new IllegalStateException("ModuleOracle expected");
        }
    }

    @Override
    public Student getStudent() {
        return new StudentOracle(studentModule.getStudent(), em);
    }

    @Override
    public Long getMark() {
        return studentModule.getMark();
    }

    @Override
    public void setMark(Long mark) {
        studentModule.setMark(mark);
    }
}

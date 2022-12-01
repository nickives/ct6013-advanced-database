package uk.edu.glos.s1909632.ct6013.backend.services;

import jakarta.ejb.EJB;
import jakarta.ejb.Stateful;
import jakarta.ejb.StatefulTimeout;
import jakarta.ws.rs.NotFoundException;
import uk.edu.glos.s1909632.ct6013.backend.DbChoice;
import uk.edu.glos.s1909632.ct6013.backend.persistence.Lecturer;
import uk.edu.glos.s1909632.ct6013.backend.persistence.Student;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Stateful(name = "UserSessionEJB")
@StatefulTimeout(
        value = 5,
        unit = TimeUnit.DAYS
)
public class UserSessionBean {
    @EJB
    DbChoiceSessionBean dbChoiceSessionBean;
    public static final class LoginResult {
        public String userId;
        public String name;
        public String courseId;
        public String destination;
    }
    private Student student;
    private Lecturer lecturer;

    public UserSessionBean() {
    }

    public Optional<LoginResult> doLogin(String userId) {
        student = null;
        lecturer = null;
        LoginResult loginResult = new LoginResult();
        student = dbChoiceSessionBean.getEntityFactory()
                .getStudent(userId)
                .orElse(null);
        if (student != null) {
            loginResult.destination = "/student";
            loginResult.userId = student.getId()
                    .orElseThrow(IllegalStateException::new);
            loginResult.name = student.getFirstName() + " " + student.getLastName();
            loginResult.courseId = student.getCourse()
                    .getId()
                    .orElseThrow(IllegalStateException::new);
            return Optional.of(loginResult);
        }

        lecturer = dbChoiceSessionBean.getEntityFactory()
                .getLecturer(userId)
                .orElse(null);
        if (lecturer != null) {
            loginResult.destination = "/lecturer";
            loginResult.userId = lecturer.getId()
                    .orElseThrow(IllegalStateException::new);
            loginResult.name = lecturer.getName();
            return Optional.of(loginResult);
        }

        return Optional.empty();
    }

    public void doLogout() {
        student = null;
        lecturer = null;
    }

    public Optional<Student> getStudent() {
        return Optional.ofNullable(student);
    }

    public Optional<Lecturer> getLecturer() {
        return Optional.ofNullable(lecturer);
    }
}

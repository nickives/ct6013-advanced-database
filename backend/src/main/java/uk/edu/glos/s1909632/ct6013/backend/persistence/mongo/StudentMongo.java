package uk.edu.glos.s1909632.ct6013.backend.persistence.mongo;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.InsertOneResult;
import org.bson.types.ObjectId;
import uk.edu.glos.s1909632.ct6013.backend.exceptions.UniqueViolation;
import uk.edu.glos.s1909632.ct6013.backend.persistence.Course;
import uk.edu.glos.s1909632.ct6013.backend.persistence.Module;
import uk.edu.glos.s1909632.ct6013.backend.persistence.Student;
import uk.edu.glos.s1909632.ct6013.backend.persistence.StudentModule;
import uk.edu.glos.s1909632.ct6013.backend.persistence.mongo.documents.CourseDocument;
import uk.edu.glos.s1909632.ct6013.backend.persistence.mongo.documents.StudentDocument;
import uk.edu.glos.s1909632.ct6013.backend.persistence.mongo.documents.StudentModuleDocument;

import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import static com.mongodb.client.model.Filters.eq;

public class StudentMongo implements Student {
    private final MongoCollection<StudentDocument> studentCollection;
    private final MongoDatabase mongoDatabase;
    private final StudentDocument student;
    private final MongoEntityFactory ef;

    public StudentMongo(MongoDatabase mongoDatabase, StudentDocument student,
                        MongoEntityFactory ef) {
        this.studentCollection = mongoDatabase.getCollection(
                MongoCollections.STUDENT.toString(), StudentDocument.class);
        this.mongoDatabase = mongoDatabase;
        this.student = student;
        this.ef = ef;
    }

    public StudentMongo(MongoDatabase mongoDatabase,
                        MongoEntityFactory ef) {
        this.studentCollection = mongoDatabase.getCollection(
                MongoCollections.STUDENT.toString(), StudentDocument.class);
        this.mongoDatabase = mongoDatabase;
        this.ef = ef;
        this.student = new StudentDocument();
    }

    @Override
    public void save() {
        if (getId().isPresent()) {
            studentCollection.findOneAndReplace(
                    eq("_id", student.getId()),
                    student
            );
        } else {
            InsertOneResult result = studentCollection.insertOne(student);
            if (result.wasAcknowledged()) {
                student.setId(Objects.requireNonNull(result.getInsertedId())
                                       .asObjectId()
                                       .getValue());
            }
        }
        // make sure student is in their course
        Course course = getCourse();
        course.addStudent(this);
        try {
            course.save();
        } catch (UniqueViolation e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<String> getId() {
        return Optional.ofNullable(student.getId()).map(ObjectId::toHexString);
    }

    @Override
    public String getFirstName() {
        return student.getFirstName();
    }

    @Override
    public void setFirstName(String firstName) {
        student.setFirstName(firstName);
    }

    @Override
    public String getLastName() {
        return student.getLastName();
    }

    @Override
    public void setLastName(String lastName) {
        student.setLastName(lastName);
    }

    @Override
    public Set<StudentModule> getModules() {
        return student.getModules()
                .stream()
                .map(sm -> new StudentModuleMongo(mongoDatabase, sm, student.getCourseId()))
                .collect(Collectors.toUnmodifiableSet());
    }

    @Override
    public Course getCourse() {
        return ef.getCourse(student.getCourseId().toHexString())
                .orElseThrow(() -> new IllegalStateException("Student not on course"));
    }

    @Override
    public void setCourse(Course course) {
        ObjectId courseId = course.getId()
                .map(ObjectId::new)
                .orElseThrow(() -> new IllegalStateException("Course missing ID"));
        student.setCourseId(courseId);
    }

    @Override
    public void addToModule(Module module) {
        StudentModuleDocument studentModuleDocument = new StudentModuleDocument();
        studentModuleDocument.setModuleDocument(((ModuleMongo) module).getEntity());
        Set<StudentModuleDocument> studentModules = student.getModules() != null
                ? student.getModules()
                : new HashSet<>();
        studentModules.add(studentModuleDocument);
        student.setModules(studentModules);
    }

    StudentDocument getDocument() {
        return student;
    }
}

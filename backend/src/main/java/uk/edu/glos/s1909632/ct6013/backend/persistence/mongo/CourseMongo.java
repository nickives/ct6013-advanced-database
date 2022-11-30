package uk.edu.glos.s1909632.ct6013.backend.persistence.mongo;

import com.mongodb.MongoWriteException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.InsertOneResult;
import org.bson.types.ObjectId;
import uk.edu.glos.s1909632.ct6013.backend.persistence.Course;
import uk.edu.glos.s1909632.ct6013.backend.exceptions.UniqueViolation;
import uk.edu.glos.s1909632.ct6013.backend.persistence.Module;
import uk.edu.glos.s1909632.ct6013.backend.persistence.Student;
import uk.edu.glos.s1909632.ct6013.backend.persistence.mongo.documents.CourseDocument;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import static com.mongodb.client.model.Filters.*;

public class CourseMongo implements Course {
    private final MongoDatabase mongoDatabase;
    private final MongoCollection<CourseDocument> courseCollection;
    private final CourseDocument course;

    public CourseMongo(MongoDatabase mongoDatabase,
                       CourseDocument course) {
        this.courseCollection = mongoDatabase.getCollection("course", CourseDocument.class);
        this.course = course;
        this.mongoDatabase = mongoDatabase;
    }

    public CourseMongo(MongoDatabase mongoDatabase) {
        this.courseCollection = mongoDatabase.getCollection("course", CourseDocument.class);
        this.course = new CourseDocument();
        this.mongoDatabase = mongoDatabase;
    }


    @Override
    public Optional<String> getId() {
        return Optional.ofNullable(course.getId()).map(ObjectId::toHexString);
    }

    @Override
    public String getName() {
        return course.getName();
    }

    @Override
    public void setName(String name) {
        course.setName(name);

    }

    @Override
    public void save() throws UniqueViolation {
        try {
            if (getId().isPresent()) {
                courseCollection.findOneAndReplace(
                        eq("_id", course.getId()),
                        course
                );
            } else {
                InsertOneResult result = courseCollection.insertOne(course);
                if (result.wasAcknowledged()) {
                    course.setId(Objects.requireNonNull(result.getInsertedId())
                                         .asObjectId()
                                         .getValue());
                }
            }
        } catch (MongoWriteException e) {
            if (e.getCode() == 11000) {
                Pattern nameText = Pattern.compile("name_text");
                if (nameText.matcher(e.getError().getMessage()).find()) {
                    throw new UniqueViolation(
                            "name",
                            "Course name already exists");
                }
            }
        }
    }

    @Override
    public void addStudent(Student student) {
        ObjectId studentId = student.getId()
                .map(ObjectId::new)
                .orElseThrow(() -> new IllegalStateException("Student missing ID"));
        course.getStudentIds().add(studentId);
    }

    @Override
    public Set<Module> getModules() {
        return course.getModules()
                .stream()
                .map(m -> new ModuleMongo(m, mongoDatabase, course.getId()))
                .collect(Collectors.toSet());
    }

    CourseDocument getCourseDocument() {
        return course;
    }
}

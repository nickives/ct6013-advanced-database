package uk.edu.glos.s1909632.ct6013.backend.persistence.mongo;

import com.mongodb.client.MongoDatabase;
import jakarta.ws.rs.NotFoundException;
import org.bson.types.ObjectId;
import uk.edu.glos.s1909632.ct6013.backend.persistence.*;
import uk.edu.glos.s1909632.ct6013.backend.persistence.Module;
import uk.edu.glos.s1909632.ct6013.backend.persistence.mongo.documents.CourseDocument;
import uk.edu.glos.s1909632.ct6013.backend.persistence.mongo.documents.LecturerDocument;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.mongodb.client.model.Filters.*;
import static uk.edu.glos.s1909632.ct6013.backend.persistence.mongo.MongoCollections.*;

public class MongoEntityFactory implements EntityFactory {

    private final MongoDatabase mongoDatabase;

    public MongoEntityFactory(MongoDatabase mongoDatabase) {
        this.mongoDatabase = mongoDatabase;
    }

    private static ObjectId getObjectId(String id) {
        ObjectId objectId;
        try {
            objectId = new ObjectId(id);
        } catch (IllegalArgumentException e) {
            throw new NotFoundException();
        }
        return objectId;
    }

    @Override
    public LecturerMongo createLecturer() {
        return new LecturerMongo(mongoDatabase);
    }

    @Override
    public Optional<Lecturer> getLecturer(String id) {
        ObjectId objectId = getObjectId(id);
        LecturerDocument lecturer = mongoDatabase.getCollection(
                LECTURER.toString(), LecturerDocument.class
                )
                .find(eq("_id", objectId))
                .first();
        return Optional.ofNullable(lecturer)
                .map(l -> new LecturerMongo(mongoDatabase, l));

    }

    @Override
    public List<Lecturer> getAllLecturers() {
        return mongoDatabase.getCollection(LECTURER.toString(), LecturerDocument.class)
                .find()
                .map(l -> new LecturerMongo(mongoDatabase, l))
                .into(new ArrayList<>());
    }

    @Override
    public ModuleMongo createModule(Course course) {
        try {
            CourseMongo courseMongo = (CourseMongo) course;
            return new ModuleMongo(courseMongo, mongoDatabase);
        } catch (ClassCastException e) {
            throw new IllegalStateException("CourseMongo expected");
        }
    }

    @Override
    public Optional<Module> getModule(String moduleId, String courseId) {
        return getCourse(courseId)
                .orElseThrow(NotFoundException::new)
                .getModules()
                .stream()
                .filter(m -> m.getId().map(moduleId::equals).orElse(false))
                .findFirst();
    }

    @Override
    public List<Module> getModules(String courseId) {
        ObjectId objectId = getObjectId(courseId);

        CourseDocument courseDocument = mongoDatabase.getCollection(COURSE.toString(),
                                                            CourseDocument.class)
                .find(eq("_id", objectId))
                .first();

        return new ArrayList<>(Optional.ofNullable(courseDocument)
                                       .map(cd -> new CourseMongo(mongoDatabase, cd, this))
                                       .orElseThrow(NotFoundException::new)
                                       .getModules());
    }

    @Override
    public CourseMongo createCourse() {
        return new CourseMongo(mongoDatabase, this);
    }

    @Override
    public Optional<Course> getCourse(String id) {
        ObjectId objectId = getObjectId(id);
        CourseDocument course = mongoDatabase.getCollection(
                    COURSE.toString(),
                    CourseDocument.class)
                .find(eq("_id", objectId))
                .first();
        return Optional.ofNullable(course)
                .map(c -> new CourseMongo(mongoDatabase, c, this));
    }

    @Override
    public List<Course> getAllCourses() {
        return mongoDatabase.getCollection(
                    COURSE.toString(),
                    CourseDocument.class)
                .find()
                .map(c -> new CourseMongo(mongoDatabase, c, this))
                .into(new ArrayList<>());
    }

    @Override
    public Student createStudent() {
        return null;
    }

    @Override
    public Optional<Student> getStudent(String id) {
        return Optional.empty();
    }

    @Override
    public List<Student> getAllStudents() {
        return null;
    }
}

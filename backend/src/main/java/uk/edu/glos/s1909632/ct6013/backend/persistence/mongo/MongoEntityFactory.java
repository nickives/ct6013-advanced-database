package uk.edu.glos.s1909632.ct6013.backend.persistence.mongo;

import com.mongodb.client.MongoDatabase;
import jakarta.ws.rs.NotFoundException;
import org.bson.types.ObjectId;
import uk.edu.glos.s1909632.ct6013.backend.persistence.*;
import uk.edu.glos.s1909632.ct6013.backend.persistence.Module;
import uk.edu.glos.s1909632.ct6013.backend.persistence.mongo.ents.LecturerMongoEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.mongodb.client.model.Filters.eq;

public class MongoEntityFactory implements EntityFactory {

    private final MongoDatabase mongoDatabase;

    public MongoEntityFactory(MongoDatabase mongoDatabase) {
        this.mongoDatabase = mongoDatabase;
    }

    @Override
    public Lecturer createLecturer() {
        return new LecturerMongo(mongoDatabase);
    }

    @Override
    public Optional<Lecturer> getLecturer(String id) {
        ObjectId objectId;
        try {
            objectId = new ObjectId(id);
        } catch (IllegalArgumentException e) {
            throw new NotFoundException();
        }
        LecturerMongoEntity lecturer = mongoDatabase.getCollection("lecturer", LecturerMongoEntity.class)
                .find(eq("_id", objectId))
                .first();
        return Optional.ofNullable(lecturer)
                .map(l -> new LecturerMongo(mongoDatabase, l));

    }

    @Override
    public List<Lecturer> getAllLecturers() {
        return mongoDatabase.getCollection("lecturer", LecturerMongoEntity.class)
                .find()
                .map(l -> new LecturerMongo(mongoDatabase, l))
                .into(new ArrayList<>());
    }

    @Override
    public Module createModule() {
        return null;
    }

    @Override
    public Optional<Module> getModule(String id) {
        return Optional.empty();
    }

    @Override
    public List<Module> getModules(String courseId) {
        return null;
    }

    @Override
    public Course createCourse() {
        return null;
    }

    @Override
    public Optional<Course> getCourse(String id) {
        return Optional.empty();
    }

    @Override
    public List<Course> getAllCourses() {
        return null;
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

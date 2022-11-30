package uk.edu.glos.s1909632.ct6013.backend.persistence.mongo;

import com.mongodb.client.MongoDatabase;
import jakarta.ws.rs.NotFoundException;
import org.bson.types.ObjectId;
import uk.edu.glos.s1909632.ct6013.backend.persistence.*;
import uk.edu.glos.s1909632.ct6013.backend.persistence.Module;
import uk.edu.glos.s1909632.ct6013.backend.persistence.mongo.documents.CourseDocument;
import uk.edu.glos.s1909632.ct6013.backend.persistence.mongo.documents.LecturerDocument;
import uk.edu.glos.s1909632.ct6013.backend.persistence.mongo.documents.StudentDocument;

import java.util.*;
import java.util.stream.Collectors;

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
            return new ModuleMongo(mongoDatabase,
                                   courseMongo.getCourseDocument().getId());
        } catch (ClassCastException e) {
            throw new IllegalStateException("CourseMongo expected");
        }
    }

    @Override
    public Optional<Module> getModuleFromCourse(String moduleId, String courseId) {
        return getCourse(courseId)
                .orElseThrow(NotFoundException::new)
                .getModules()
                .stream()
                .filter(m -> m.getId().map(moduleId::equals).orElse(false))
                .findFirst();
    }

    @Override
    public List<Module> getModulesFromCourse(List<String> moduleIds, String courseId) {
        Set<String> moduleIdSet = new HashSet<>(moduleIds);
        CourseDocument courseDocument = getCourseDocument(getObjectId(courseId));
        return courseDocument.getModules()
                .stream()
                .filter(m -> moduleIdSet.contains(m.getId().toHexString()))
                .map(m -> new ModuleMongo(m, mongoDatabase, getObjectId(courseId)))
                .collect(Collectors.toUnmodifiableList());
    }

    @Override
    public List<Module> getAllCourseModules(String courseId) {
        ObjectId objectId = getObjectId(courseId);

        CourseDocument courseDocument = getCourseDocument(
                objectId);

        return new ArrayList<>(Optional.ofNullable(courseDocument)
                                       .map(cd -> new CourseMongo(mongoDatabase, cd))
                                       .orElseThrow(NotFoundException::new)
                                       .getModules());
    }

    @Override
    public List<StudentModule> getAllStudentModules(String studentId) {
        StudentDocument studentDocument = getStudentDocument(getObjectId(studentId))
                .orElseThrow(NotFoundException::new);
        ObjectId courseId = studentDocument.getCourseId();
        return studentDocument
                .getModules()
                .stream()
                .map(smd -> new StudentModuleMongo(mongoDatabase, smd, courseId))
                .collect(Collectors.toUnmodifiableList());
    }

    @Override
    public Optional<StudentModule> getStudentModule(String studentId, String moduleId) {
        StudentDocument studentDocument = getStudentDocument(getObjectId(studentId))
                .orElseThrow(NotFoundException::new);
        ObjectId courseId = studentDocument.getCourseId();
        return studentDocument
                .getModules()
                .stream()
                .filter(m -> m.getModuleDocument().getId().equals(getObjectId(moduleId)))
                .findFirst()
                .map(smd -> new StudentModuleMongo(mongoDatabase, smd, courseId));

    }

    @Override
    public CourseMongo createCourse() {
        return new CourseMongo(mongoDatabase);
    }

    @Override
    public Optional<Course> getCourse(String id) {
        ObjectId objectId = getObjectId(id);
        CourseDocument course = getCourseDocument(
                objectId);
        return Optional.ofNullable(course)
                .map(c -> new CourseMongo(mongoDatabase, c));
    }

    private CourseDocument getCourseDocument(ObjectId objectId) {
        return mongoDatabase.getCollection(
                    COURSE.toString(),
                    CourseDocument.class)
                .find(eq("_id", objectId))
                .first();
    }

    @Override
    public List<Course> getAllCourses() {
        return mongoDatabase.getCollection(
                    COURSE.toString(),
                    CourseDocument.class)
                .find()
                .map(c -> new CourseMongo(mongoDatabase, c))
                .into(new ArrayList<>());
    }

    @Override
    public Student createStudent() {
        return new StudentMongo(mongoDatabase, this);
    }

    @Override
    public Optional<Student> getStudent(String id) {
        ObjectId objectId = getObjectId(id);
        return getStudentDocument(objectId)
                .map(s -> new StudentMongo(mongoDatabase, s, this));
    }

    private Optional<StudentDocument> getStudentDocument(ObjectId objectId) {
        return Optional.ofNullable(mongoDatabase.getCollection(
                        STUDENT.toString(),
                        StudentDocument.class)
                .find(eq("_id", objectId))
                .first());
    }

    @Override
    public List<Student> getAllStudents() {
        return mongoDatabase.getCollection(
                        STUDENT.toString(),
                        StudentDocument.class)
                .find()
                .map(s -> new StudentMongo(mongoDatabase, s, this))
                .into(new ArrayList<>());
    }
}

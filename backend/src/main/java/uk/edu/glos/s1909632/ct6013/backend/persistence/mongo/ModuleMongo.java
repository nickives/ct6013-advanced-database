package uk.edu.glos.s1909632.ct6013.backend.persistence.mongo;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.types.ObjectId;
import uk.edu.glos.s1909632.ct6013.backend.exceptions.UniqueViolation;
import uk.edu.glos.s1909632.ct6013.backend.persistence.Lecturer;
import uk.edu.glos.s1909632.ct6013.backend.persistence.Module;
import uk.edu.glos.s1909632.ct6013.backend.persistence.StudentModule;
import uk.edu.glos.s1909632.ct6013.backend.persistence.mongo.documents.CourseDocument;
import uk.edu.glos.s1909632.ct6013.backend.persistence.mongo.documents.ModuleDocument;
import uk.edu.glos.s1909632.ct6013.backend.persistence.mongo.documents.StudentDocument;

import java.util.*;
import java.util.stream.Collectors;

import static com.mongodb.client.model.Filters.eq;

public class ModuleMongo implements Module {
    private final MongoDatabase mongoDatabase;
    private final MongoEntityFactory ef;
    private final ModuleDocument module;
    private final ObjectId courseId;

    /***
     * ModuleMongo - Construct from module retrieved from db
     * @param module Module document from db
     * @param mongoDatabase Database connection
     * @param ef MongoEntityFactory instance
     */
    public ModuleMongo(ModuleDocument module,
                       MongoDatabase mongoDatabase,
                       MongoEntityFactory ef, ObjectId courseId) {
        this.module = module;
        this.mongoDatabase = mongoDatabase;
        this.ef = ef;
        this.courseId = courseId;
    }

    /***
     * ModuleMongo - Construct new module
     * @param mongoDatabase Database connection
     * @param ef MongoEntityFactory instance
     * @param courseId ID of associated course
     */
    public ModuleMongo(MongoDatabase mongoDatabase,
                       MongoEntityFactory ef, ObjectId courseId) {
        this.ef = ef;
        this.courseId = courseId;
        this.module = new ModuleDocument();
        this.mongoDatabase = mongoDatabase;
    }

    @Override
    public void save() throws UniqueViolation {
        if (getId().isEmpty()) {
            module.setId(new ObjectId());
        }
        // Save module in course
        // Because MongoDB can't enforce unique constraints within document, check
        // module code uniqueness manually
        MongoCollection<CourseDocument> courseCollection = mongoDatabase.getCollection(
                MongoCollections.COURSE.toString(), CourseDocument.class);
        CourseDocument course = courseCollection
                .find(eq("_id", courseId))
                .first();

        if (course == null) throw new IllegalStateException();

        if (course.getModules().contains(module)) throw new UniqueViolation(
                "code",
                "Module code already exists"
        );

        course.getModules().add(module);
        new CourseMongo(mongoDatabase, course, ef).save();
    }

    @Override
    public Optional<String> getId() {
        return Optional.ofNullable(module.getId()).map(ObjectId::toHexString);
    }

    @Override
    public String getName() {
        return module.getName();
    }

    @Override
    public void setName(String name) {
        module.setName(name);
    }

    @Override
    public String getCode() {
        return module.getCode();
    }

    @Override
    public void setCode(String code) {
        module.setCode(code);
    }

    @Override
    public String getSemester() {
        return module.getSemester();
    }

    @Override
    public void setSemester(String semester) {
        module.setSemester(semester);
    }

    @Override
    public Long getCatPoints() {
        return module.getCatPoints();
    }

    @Override
    public void setCatPoints(Long catPoints) {
        module.setCatPoints(catPoints);
    }

    @Override
    public Lecturer getLecturer() {
        return Optional.ofNullable(module.getLecturer())
                .map(l -> new LecturerMongo(mongoDatabase, l))
                .orElse(new LecturerMongo(mongoDatabase));
    }

    @Override
    public void setLecturer(Lecturer lecturer) {
        try {
            LecturerMongo lecturerMongo = (LecturerMongo) lecturer;
            module.setLecturer(lecturerMongo.getDocument());
        } catch (ClassCastException e) {
            throw new IllegalStateException("LecturerMongo expected");
        }
    }

    @Override
    public Set<StudentModule> getStudentModules() {
        List<StudentDocument> students = new ArrayList<>();
        mongoDatabase.getCollection(MongoCollections.STUDENT.toString(),
                                           StudentDocument.class)
                .find(eq("modules.moduleDocument._id", module.getId()))
                .into(students);
        return students.stream()
                .flatMap(student -> student.getModules()
                        .stream()
                        .filter(m -> m.getModuleDocument().getId().equals(module.getId()))
                        .map(sm -> new StudentModuleMongo(mongoDatabase, sm, courseId, student.getId(), ef)))
                .collect(Collectors.toUnmodifiableSet());
    }

    @Override
    public String getCourseId() {
        return courseId.toHexString();
    }

    ModuleDocument getEntity() {
        return module;
    }
}

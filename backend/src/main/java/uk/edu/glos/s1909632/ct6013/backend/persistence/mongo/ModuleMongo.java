package uk.edu.glos.s1909632.ct6013.backend.persistence.mongo;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import uk.edu.glos.s1909632.ct6013.backend.exceptions.UniqueViolation;
import uk.edu.glos.s1909632.ct6013.backend.persistence.Lecturer;
import uk.edu.glos.s1909632.ct6013.backend.persistence.Module;
import uk.edu.glos.s1909632.ct6013.backend.persistence.Student;
import uk.edu.glos.s1909632.ct6013.backend.persistence.mongo.documents.CourseDocument;
import uk.edu.glos.s1909632.ct6013.backend.persistence.mongo.documents.ModuleDocument;
import java.util.Optional;
import java.util.Set;

public class ModuleMongo implements Module {
    private final MongoDatabase mongoDatabase;
    private final ModuleDocument module;
    private final ObjectId courseId;

    /***
     * ModuleMongo - Construct from module retrieved from db
     * @param module Module document from db
     * @param mongoDatabase Database connection
     */
    public ModuleMongo(ModuleDocument module,
                       MongoDatabase mongoDatabase,
                       ObjectId courseId) {
        this.module = module;
        this.mongoDatabase = mongoDatabase;
        this.courseId = courseId;
    }

    /***
     * ModuleMongo - Construct new module
     * @param mongoDatabase Database connection
     * @param courseId ID of associated course
     */
    public ModuleMongo(MongoDatabase mongoDatabase,
                       ObjectId courseId) {
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
        MongoCollection<CourseDocument> courseCollection = mongoDatabase.getCollection(
                MongoCollections.COURSE.toString(), CourseDocument.class);
        Bson filter = Filters.eq("_id", courseId);
        Bson update = Updates.addToSet("modules", module);
        courseCollection.findOneAndUpdate(filter, update);
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
    public Set<Student> getStudents() {
        return null;
    }

    ModuleDocument getEntity() {
        return module;
    }
}

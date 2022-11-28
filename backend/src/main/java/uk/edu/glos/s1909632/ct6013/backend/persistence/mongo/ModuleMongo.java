package uk.edu.glos.s1909632.ct6013.backend.persistence.mongo;
import com.mongodb.client.MongoDatabase;
import org.bson.types.ObjectId;
import uk.edu.glos.s1909632.ct6013.backend.exceptions.UniqueViolation;
import uk.edu.glos.s1909632.ct6013.backend.persistence.Course;
import uk.edu.glos.s1909632.ct6013.backend.persistence.Lecturer;
import uk.edu.glos.s1909632.ct6013.backend.persistence.Module;
import uk.edu.glos.s1909632.ct6013.backend.persistence.mongo.documents.CourseDocument;

import java.util.Optional;


public class ModuleMongo implements Module {
    private final MongoDatabase mongoDatabase;
    private final CourseDocument.CourseModuleMongoEntity module;
    private CourseMongo course;

    /***
     * ModuleMongo - Construct from module retrieved from db
     * @param module Module document from db
     * @param course Course
     * @param mongoDatabase Database connection
     */
    public ModuleMongo(CourseDocument.CourseModuleMongoEntity module, CourseMongo course,
                       MongoDatabase mongoDatabase
    ) {
        this.module = module;
        this.course = course;
        this.mongoDatabase = mongoDatabase;
    }

    /***
     * ModuleMongo - Construct new module
     * @param course Course
     * @param mongoDatabase Database connection
     */
    public ModuleMongo(CourseMongo course, MongoDatabase mongoDatabase) {
        this.module = new CourseDocument.CourseModuleMongoEntity();
        this.course = course;
        this.mongoDatabase = mongoDatabase;
    }

    @Override
    public void save() throws UniqueViolation {
        if (getId().isEmpty()) {
            module.setId(new ObjectId());
        }
        // Save module in course
        course.addModule(this);
        course.save();

        // Get lecturer and make sure this module is in their moduleIds
//        Lecturer lecturer = ef.getLecturer(module.getLecturer().getId().toHexString())
//                .orElseThrow(IllegalStateException::new);
//        lecturer.getModules().add(this);
//        lecturer.save();
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
    public Number getCatPoints() {
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

//    @Override
//    public Set<Student> getStudents() {
//        return null;
//    }
//
//    @Override
//    public void setStudents(Set<Student> students) {
//
//    }

    @Override
    public Course getCourse() {
        return course;
    }

    @Override
    public void setCourse(Course course)  {
        try {
            this.course = (CourseMongo) course;
        } catch (ClassCastException e) {
            throw new IllegalStateException("CourseMongo expected", e);
        }
    }

    CourseDocument.CourseModuleMongoEntity getEntity() {
        return module;
    }
}

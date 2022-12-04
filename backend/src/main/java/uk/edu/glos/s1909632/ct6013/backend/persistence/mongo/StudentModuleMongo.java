package uk.edu.glos.s1909632.ct6013.backend.persistence.mongo;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.types.ObjectId;
import uk.edu.glos.s1909632.ct6013.backend.persistence.Module;
import uk.edu.glos.s1909632.ct6013.backend.persistence.Student;
import uk.edu.glos.s1909632.ct6013.backend.persistence.StudentModule;
import uk.edu.glos.s1909632.ct6013.backend.persistence.mongo.documents.ModuleDocument;
import uk.edu.glos.s1909632.ct6013.backend.persistence.mongo.documents.StudentDocument;
import uk.edu.glos.s1909632.ct6013.backend.persistence.mongo.documents.StudentModuleDocument;

import static com.mongodb.client.model.Filters.eq;

public class StudentModuleMongo implements StudentModule {

    private final MongoDatabase mongoDatabase;
    private final MongoCollection<StudentDocument> studentCollection;
    private final StudentModuleDocument studentModuleDocument;
    private final ObjectId courseId;
    private final ObjectId studentId;
    private final MongoEntityFactory ef;

    public StudentModuleMongo(MongoDatabase mongoDatabase,
                              StudentModuleDocument studentModuleDocument,
                              ObjectId courseId, ObjectId studentId,
                              MongoEntityFactory ef) {
        this.mongoDatabase = mongoDatabase;
        this.studentCollection = mongoDatabase.getCollection(
                MongoCollections.STUDENT.toString(), StudentDocument.class);
        this.studentModuleDocument = studentModuleDocument;
        this.courseId = courseId;
        this.studentId = studentId;
        this.ef = ef;
    }

    @Override
    public Module getModule() {
        ModuleDocument moduleDocument = studentModuleDocument.getModuleDocument();
        return new ModuleMongo(moduleDocument, mongoDatabase, ef, courseId);
    }

    @Override
    public void setModule(Module module) {
        try {
            ModuleMongo moduleMongo = (ModuleMongo) module;
            studentModuleDocument.setModuleDocument(moduleMongo.getEntity());
        } catch (ClassCastException e) {
            throw new IllegalStateException("ModuleMongo expected", e);
        }
    }

    @Override
    public Student getStudent() {
        StudentDocument studentDocument = studentCollection.find(eq("_id", studentId))
                .first();
        return new StudentMongo(mongoDatabase, studentDocument, ef);
    }

    @Override
    public Long getMark() {
        return studentModuleDocument.getMark();
    }

    @Override
    public StudentModule setMark(Long mark) {
        studentModuleDocument.setMark(mark);
        MongoCollection<StudentDocument> studentCollection = mongoDatabase.getCollection(
                MongoCollections.STUDENT.toString(), StudentDocument.class);
        StudentDocument studentDocument = studentCollection.find(eq("_id", studentId))
                .first();
        studentDocument.getModules().remove(studentModuleDocument);
        studentDocument.getModules().add(studentModuleDocument);
        studentCollection.findOneAndReplace(eq("_id", studentId), studentDocument);
        return this;
    }
}

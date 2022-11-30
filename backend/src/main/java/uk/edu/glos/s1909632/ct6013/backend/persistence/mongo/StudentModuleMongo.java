package uk.edu.glos.s1909632.ct6013.backend.persistence.mongo;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.types.ObjectId;
import uk.edu.glos.s1909632.ct6013.backend.persistence.Module;
import uk.edu.glos.s1909632.ct6013.backend.persistence.StudentModule;
import uk.edu.glos.s1909632.ct6013.backend.persistence.mongo.documents.ModuleDocument;
import uk.edu.glos.s1909632.ct6013.backend.persistence.mongo.documents.StudentModuleDocument;

public class StudentModuleMongo implements StudentModule {

    private final MongoDatabase mongoDatabase;
    private final MongoCollection<StudentModuleDocument> studentCollection;
    private final StudentModuleDocument studentModuleDocument;
    private final ObjectId courseId;

    public StudentModuleMongo(MongoDatabase mongoDatabase,
                              StudentModuleDocument studentModuleDocument,
                              ObjectId courseId) {
        this.mongoDatabase = mongoDatabase;
        this.studentCollection = mongoDatabase.getCollection(
                MongoCollections.STUDENT.toString(), StudentModuleDocument.class);
        this.studentModuleDocument = studentModuleDocument;
        this.courseId = courseId;
    }

    public StudentModuleMongo(MongoDatabase mongoDatabase, ObjectId courseId) {
        this.mongoDatabase = mongoDatabase;
        this.studentCollection = mongoDatabase.getCollection(
                MongoCollections.STUDENT.toString(), StudentModuleDocument.class);
        this.courseId = courseId;
        this.studentModuleDocument = new StudentModuleDocument();
    }

    @Override
    public Module getModule() {
        ModuleDocument moduleDocument = studentModuleDocument.getModuleDocument();
        return new ModuleMongo(moduleDocument, mongoDatabase, courseId);
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
    public Long getMark() {
        return studentModuleDocument.getMark();
    }

    @Override
    public void setMark(Long mark) {
        studentModuleDocument.setMark(mark);
    }
}

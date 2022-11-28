package uk.edu.glos.s1909632.ct6013.backend.persistence.mongo;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.InsertOneResult;
import org.bson.types.ObjectId;
import uk.edu.glos.s1909632.ct6013.backend.persistence.Lecturer;
import uk.edu.glos.s1909632.ct6013.backend.persistence.Module;
import uk.edu.glos.s1909632.ct6013.backend.persistence.mongo.documents.LecturerDocument;

import java.util.*;

import static com.mongodb.client.model.Filters.*;

public class LecturerMongo implements Lecturer {
    private final MongoDatabase mongoDatabase;
    private final MongoCollection<LecturerDocument> lecturerCollection;
    private final LecturerDocument lecturer;

    protected LecturerMongo(
            MongoDatabase mongoDatabase,
            LecturerDocument lecturerDocument
    ) {
        this.lecturerCollection = mongoDatabase.getCollection("lecturer", LecturerDocument.class);
        this.lecturer = lecturerDocument;
        this.mongoDatabase = mongoDatabase;
    }

    protected LecturerMongo(MongoDatabase mongoDatabase) {
        this.lecturerCollection = mongoDatabase.getCollection("lecturer", LecturerDocument.class);
        this.lecturer = new LecturerDocument();
        this.mongoDatabase = mongoDatabase;
    }

    @Override
    public void save() {
        if (getId().isPresent()) {
            lecturerCollection.findOneAndReplace(
                    and(eq("_id", getId())),
                    lecturer
            );
        } else {
            InsertOneResult result = lecturerCollection.insertOne(lecturer);
            if (result.wasAcknowledged()) {
                lecturer.setId(Objects.requireNonNull(result.getInsertedId())
                                       .asObjectId()
                                       .getValue());
            }
        }
    }

    @Override
    public Optional<String> getId() {
        return Optional.ofNullable(lecturer.getId()).map(ObjectId::toHexString);
    }

    @Override
    public void setName(String name) {
        lecturer.setName(name);
    }

    @Override
    public String getName() {
        return lecturer.getName();
    }

    @Override
    public Set<Module> getModules() {
//        MongoCollection<CourseDocument> courseCollection = mongoDatabase.getCollection(
//                MongoCollections.COURSE.toString(), CourseDocument.class);
//        List<CourseDocument.CourseModuleMongoEntity> moduleDocuments = courseCollection.
        return null;
    }

    LecturerDocument getDocument() {
        return lecturer;
    }
}

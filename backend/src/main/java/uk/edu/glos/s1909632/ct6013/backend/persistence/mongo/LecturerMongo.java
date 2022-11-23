package uk.edu.glos.s1909632.ct6013.backend.persistence.mongo;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.InsertOneResult;
import org.bson.types.ObjectId;
import uk.edu.glos.s1909632.ct6013.backend.persistence.Lecturer;
import uk.edu.glos.s1909632.ct6013.backend.persistence.Module;
import uk.edu.glos.s1909632.ct6013.backend.persistence.mongo.ents.LecturerMongoEntity;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;

public class LecturerMongo implements Lecturer {

    private final MongoCollection<LecturerMongoEntity> lecturerCollection;
    private final LecturerMongoEntity lecturer;

    protected LecturerMongo(
            MongoDatabase mongoDatabase,
            LecturerMongoEntity lecturerMongoEntity
    ) {
        this.lecturerCollection = mongoDatabase.getCollection("lecturer", LecturerMongoEntity.class);
        this.lecturer = lecturerMongoEntity;
    }

    protected LecturerMongo(MongoDatabase mongoDatabase) {
        this.lecturerCollection = mongoDatabase.getCollection("lecturer", LecturerMongoEntity.class);
        this.lecturer = new LecturerMongoEntity();
    }

    @Override
    public void save() {
        if (getId().isPresent()) {
            lecturerCollection.findOneAndReplace(
                    and(eq("id", getId())),
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
    public void setModules(Set<Module> modules) {
//        lecturer.setModules(modules);
    }

    @Override
    public Set<Module> getModules() {
        return null;
    }
}

package uk.edu.glos.s1909632.ct6013.backend.persistence.mongo;

import com.mongodb.MongoWriteException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.InsertOneResult;
import org.bson.types.ObjectId;
import uk.edu.glos.s1909632.ct6013.backend.persistence.Course;
import uk.edu.glos.s1909632.ct6013.backend.persistence.exceptions.UniqueViolation;
import uk.edu.glos.s1909632.ct6013.backend.persistence.mongo.ents.CourseMongoEntity;

import java.util.Objects;
import java.util.Optional;
import java.util.regex.Pattern;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;

public class CourseMongo implements Course {
    private final MongoCollection<CourseMongoEntity> courseCollection;
    private final CourseMongoEntity course;

    public CourseMongo(MongoDatabase mongoDatabase,
                       CourseMongoEntity course) {
        this.courseCollection = mongoDatabase.getCollection("course", CourseMongoEntity.class);
        this.course = course;
    }

    public CourseMongo(MongoDatabase mongoDatabase) {
        this.courseCollection = mongoDatabase.getCollection("course", CourseMongoEntity.class);
        this.course = new CourseMongoEntity();
    }


    @Override
    public Optional<String> getId() {
        return Optional.ofNullable(course.getId()).map(ObjectId::toHexString);
    }

    @Override
    public String getName() {
        return course.getName();
    }

    @Override
    public void setName(String name) {
        course.setName(name);
    }

    @Override
    public void save() throws UniqueViolation {
        try {
            if (getId().isPresent()) {
                courseCollection.findOneAndReplace(
                        and(eq("_id", getId())),
                        course
                );
            } else {
                InsertOneResult result = courseCollection.insertOne(course);
                if (result.wasAcknowledged()) {
                    course.setId(Objects.requireNonNull(result.getInsertedId())
                                         .asObjectId()
                                         .getValue());
                }
            }
        } catch (MongoWriteException e) {
            if (e.getCode() == 11000) {
                Pattern nameText = Pattern.compile("name_text");
                if (nameText.matcher(e.getError().getMessage()).find()) {
                    throw new UniqueViolation("name");
                }
            }
        }
    }
}

package uk.edu.glos.s1909632.ct6013.backend.persistence.mongo;

import com.mongodb.MongoWriteException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.InsertOneResult;
import org.bson.types.ObjectId;
import uk.edu.glos.s1909632.ct6013.backend.persistence.Course;
import uk.edu.glos.s1909632.ct6013.backend.exceptions.UniqueViolation;
import uk.edu.glos.s1909632.ct6013.backend.persistence.Module;
import uk.edu.glos.s1909632.ct6013.backend.persistence.mongo.documents.CourseDocument;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.mongodb.client.model.Filters.*;

public class CourseMongo implements Course {
    private final MongoEntityFactory ef;
    private final MongoDatabase mongoDatabase;
    private final MongoCollection<CourseDocument> courseCollection;
    private final CourseDocument course;

    public CourseMongo(MongoDatabase mongoDatabase,
                       CourseDocument course,
                       MongoEntityFactory ef) {
        this.courseCollection = mongoDatabase.getCollection("course", CourseDocument.class);
        this.course = course;
        this.mongoDatabase = mongoDatabase;
        this.ef = ef;
    }

    public CourseMongo(MongoDatabase mongoDatabase, MongoEntityFactory ef) {
        this.courseCollection = mongoDatabase.getCollection("course", CourseDocument.class);
        this.course = new CourseDocument();
        this.mongoDatabase = mongoDatabase;
        this.ef = ef;
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
                        and(eq("_id", course.getId())),
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
                    throw new UniqueViolation(
                            "name",
                            "Course name already exists");
                }
            }
        }
    }

    @Override
    public Set<Module> getModules() {
        return course.getModules()
                .stream()
                .map(m -> new ModuleMongo(m, this, mongoDatabase))
                .collect(Collectors.toSet());
    }

    @Override
    public void addModule(Module module) {
        try {
            ModuleMongo moduleMongo = (ModuleMongo) module;
            course.getModules().add(moduleMongo.getEntity());
        } catch (ClassCastException e) {
            throw new IllegalStateException("ModuleMongo expected", e);
        }
    }

    CourseDocument getCourseDocument() {
        return course;
    }
}

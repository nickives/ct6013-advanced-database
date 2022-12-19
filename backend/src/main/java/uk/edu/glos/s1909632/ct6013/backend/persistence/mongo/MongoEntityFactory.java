package uk.edu.glos.s1909632.ct6013.backend.persistence.mongo;

import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Accumulators;
import com.mongodb.client.model.Aggregates;
import jakarta.ws.rs.NotFoundException;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import uk.edu.glos.s1909632.ct6013.backend.CourseStats;
import uk.edu.glos.s1909632.ct6013.backend.Grade;
import uk.edu.glos.s1909632.ct6013.backend.persistence.*;
import uk.edu.glos.s1909632.ct6013.backend.persistence.Module;
import uk.edu.glos.s1909632.ct6013.backend.persistence.mongo.documents.*;

import java.util.*;
import java.util.stream.Collectors;

import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Projections.fields;
import static com.mongodb.client.model.Projections.include;
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
                                   this, courseMongo.getCourseDocument().getId());
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
    public Optional<Module> getModuleFromLecturer(String moduleId, String lecturerId) {
        ObjectId moduleObjectId = getObjectId(moduleId);
        ObjectId lecturerObjectId = getObjectId(lecturerId);
        CourseDocument course = Optional.ofNullable(
                mongoDatabase.getCollection(COURSE.toString(), CourseDocument.class)
                        .find(and(
                                eq("modules._id", moduleObjectId),
                                eq("modules.lecturer._id", lecturerObjectId)))
                        .first())
                .orElseThrow(NotFoundException::new);

        return course.getModules()
                .stream()
                .filter(md -> md.getId().equals(moduleObjectId))
                .findFirst()
                .map(md -> new ModuleMongo(md, mongoDatabase, this, course.getId()));
    }

    @Override
    public List<Module> getModulesFromCourse(List<String> moduleIds, String courseId) {
        Set<String> moduleIdSet = new HashSet<>(moduleIds);
        CourseDocument courseDocument = getCourseDocument(getObjectId(courseId));
        return courseDocument.getModules()
                .stream()
                .filter(m -> moduleIdSet.contains(m.getId().toHexString()))
                .map(m -> new ModuleMongo(m, mongoDatabase, this, getObjectId(courseId)))
                .collect(Collectors.toUnmodifiableList());
    }

    @Override
    public List<Module> getAllCourseModules(String courseId) {
        ObjectId objectId = getObjectId(courseId);

        CourseDocument courseDocument = getCourseDocument(objectId);

        return new ArrayList<>(Optional.ofNullable(courseDocument)
                                       .map(cd -> new CourseMongo(mongoDatabase, cd, this))
                                       .orElseThrow(NotFoundException::new)
                                       .getModules());
    }

    @Override
    public List<StudentModule> getAllStudentModules(String studentId) {
        ObjectId studentObjectId = getObjectId(studentId);
        StudentDocument studentDocument = getStudentDocument(studentObjectId)
                .orElseThrow(NotFoundException::new);
        ObjectId courseId = studentDocument.getCourseId();
        return studentDocument
                .getModules()
                .stream()
                .map(smd -> new StudentModuleMongo(mongoDatabase, smd, courseId, studentObjectId, this))
                .collect(Collectors.toUnmodifiableList());
    }

    @Override
    public List<Module> getAllLecturerModules(String lecturerId) {
        ObjectId lecturerObjectId = getObjectId(lecturerId);
        List<CourseDocument> courses = new ArrayList<>();
        mongoDatabase.getCollection(COURSE.toString(), CourseDocument.class)
                .find(eq("modules.lecturer._id", lecturerObjectId))
                .projection(include("modules"))
                .into(courses);

        return courses.stream()
                .flatMap(c -> c.getModules()
                        .stream()
                        .filter(m -> m.getLecturer().getId().equals(lecturerObjectId))
                        .map(module -> new ModuleMongo(module, mongoDatabase, this, c.getId())))
                .collect(Collectors.toUnmodifiableList());

    }

    @Override
    public Optional<StudentModule> getStudentModule(String studentId, String moduleId) {
        ObjectId studentObjectId = getObjectId(studentId);
        StudentDocument studentDocument = getStudentDocument(studentObjectId)
                .orElseThrow(NotFoundException::new);
        ObjectId courseId = studentDocument.getCourseId();
        return studentDocument
                .getModules()
                .stream()
                .filter(m -> m.getModuleDocument().getId().equals(getObjectId(moduleId)))
                .findFirst()
                .map(smd -> new StudentModuleMongo(mongoDatabase, smd, courseId, studentObjectId, this));
    }

    @Override
    public CourseMongo createCourse() {
        return new CourseMongo(mongoDatabase, this);
    }

    @Override
    public Optional<Course> getCourse(String id) {
        ObjectId objectId = getObjectId(id);
        CourseDocument course = getCourseDocument(
                objectId);
        return Optional.ofNullable(course)
                .map(c -> new CourseMongo(mongoDatabase, c, this));
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
                .map(c -> new CourseMongo(mongoDatabase, c, this))
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

    private static Document getGradeCondition(Grade grade) {
        return new Document()
                .append("$cond", new Document()
                        .append("if", new Document()
                                .append("$eq", Arrays.asList("$grade", grade)))
                        .append("then", 1)
                        .append("else", 0));
    }

    @Override
    public List<CourseStats> getCourseStats() {
        final Document firstGradeCondition =  getGradeCondition(Grade.FIRST);
        final Document twoOneGradeCondition =  getGradeCondition(Grade.TWO_ONE);
        final Document twoTwoGradeCondition =  getGradeCondition(Grade.TWO_TWO);
        final Document thirdGradeCondition =  getGradeCondition(Grade.THIRD);
        final Document failGradeCondition =  getGradeCondition(Grade.FAIL);

        final Bson courseProjectionFields = fields(
                eq("courseId", "$courseId"),
                eq("courseName", "$courseName"),
                eq("mark", "$modules.mark"),
                eq("grade", "$grade"));

        final List<CourseStatsDocument> courseStatsDocuments = new ArrayList<>();
        mongoDatabase.getCollection(STUDENT.toString(), CourseStatsDocument.class)
                .aggregate(List.of(
                        Aggregates.unwind("$modules"),
                        Aggregates.project(courseProjectionFields),
                        Aggregates.group(fields(
                                eq("courseId", "$courseId"),
                                eq("courseName", "$courseName")),
                            Accumulators.avg("averageMark", "$mark"),
                            Accumulators.sum("first", firstGradeCondition),
                            Accumulators.sum("twoOne", twoOneGradeCondition),
                            Accumulators.sum("twoTwo", twoTwoGradeCondition),
                            Accumulators.sum("third", thirdGradeCondition),
                            Accumulators.sum("fail", failGradeCondition)
                        )))
                .into(courseStatsDocuments);

        final Bson moduleProjectionFields = fields(
                eq("courseId", "$courseId"),
                eq("courseName", "$courseName"),
                eq("moduleId", "$modules.moduleDocument._id"),
                eq("moduleName", "$modules.moduleDocument.name"),
                eq("mark", "$modules.mark")
        );

        final List<ModuleStatsDocument> moduleStatsDocuments = new ArrayList<>();
        mongoDatabase.getCollection(STUDENT.toString(), ModuleStatsDocument.class)
                .aggregate(List.of(
                        Aggregates.unwind("$modules"),
                        Aggregates.project(moduleProjectionFields),
                        Aggregates.group(fields(
                                eq("courseId", "$courseId"),
                                eq("courseName", "$courseName"),
                                eq("moduleId", "$moduleId"),
                                eq("moduleName", "$moduleName")),
                            Accumulators.avg("averageMark", "$mark"),
                            Accumulators.sum("numberOfStudents", 1)
                        )))
                .into(moduleStatsDocuments);

        Map<ObjectId, List<CourseStats.ModuleStats>> moduleStats = moduleStatsDocuments
                .stream()
                .collect(Collectors.toMap(
                        // Key mapper
                        msd -> msd.getId().getCourseId(),
                        // Value mapper
                        msd -> List.of(new CourseStats.ModuleStats(
                                msd.getId().getModuleId().toHexString(),
                                msd.getId().getModuleName(),
                                msd.getAverageMark(),
                                msd.getNumberOfStudents())),
                        (previous, current) -> {
                            List<CourseStats.ModuleStats> newList = new ArrayList<>(previous);
                            newList.addAll(current);
                            return newList;
                        }));

        return courseStatsDocuments.stream()
                .map(csd -> new CourseStats(
                        csd.getId().getCourseId().toHexString(),
                        csd.getId().getCourseName(),
                        moduleStats.get(csd.getId().getCourseId()),
                        csd.getAverageMark(), csd.getFirst(), csd.getTwoOne(),
                        csd.getTwoTwo(), csd.getThird(), csd.getFail()))
                .collect(Collectors.toUnmodifiableList());
    }
}

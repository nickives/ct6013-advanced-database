package uk.edu.glos.s1909632.ct6013.integration;

import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;

@Testcontainers
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class StudentRESTTest extends BaseIntegrationTest {
    final private static class StudentTestData {
        final List<Map<String, Object>> oracle = new ArrayList<>();
        final List<Map<String, Object>> mongo = new ArrayList<>();
    }
    final private static StudentTestData testData = new StudentTestData();

    static private URL appUrl;

    StudentRESTTest() {}

    private static class TestIdContainer {
        String lecturer;
        List<String> module = new ArrayList<>();
        Map<String, Map<String, Object>> moduleData = new HashMap<>();
        Map<String, List<Map<String, Object>>> moduleMarks = new HashMap<>();
        String course;
    }
    private static final TestIdContainer oracleTestIds = new TestIdContainer();
    private static final TestIdContainer mongoTestIds = new TestIdContainer();

    @BeforeAll
    static public void setAppUrl() throws MalformedURLException {
        appUrl = new URL(baseAppUrl + "/student");
    }


    @BeforeAll
    static public void initStudents() throws MalformedURLException {
        HashMap<String, String> lecturerOracle = new HashMap<>();
        lecturerOracle.put("name", "John Doe");
        URL lecturerURLOracle = new URL(baseAppUrl + "/lecturer");
        Response response = given().
                contentType(ContentType.JSON).
                body(lecturerOracle).
        when().
                post(lecturerURLOracle).
        then().
                statusCode(201).
        extract().
                response();
        String body = response.getBody().asString();
        JsonPath jsonPath = new JsonPath(body);
        oracleTestIds.lecturer = jsonPath.getString("id");

        HashMap<String, String> lecturerMongo = new HashMap<>();
        lecturerMongo.put("name", "John Doe");
        URL lecturerURLMongo = new URL(baseAppUrl + "/lecturer?db=mongo");
        response = given().
                contentType(ContentType.JSON).
                body(lecturerMongo).
        when().
                post(lecturerURLMongo).
        then().
                statusCode(201).
        extract().
                response();
        body = response.getBody().asString();
        jsonPath = new JsonPath(body);
        mongoTestIds.lecturer = jsonPath.getString("id");

        HashMap<String, String> courseOracle = new HashMap<>();
        courseOracle.put("name", "Test Course");
        URL courseOracleURL = new URL(baseAppUrl + "/course");
        response = given().
                contentType(ContentType.JSON).
                body(courseOracle).
        when().
                post(courseOracleURL).
        then().
                statusCode(201).
        extract().
                response();
        body = response.getBody().asString();
        jsonPath = new JsonPath(body);
        oracleTestIds.course = jsonPath.getString("id");

        HashMap<String, String> courseMongo = new HashMap<>();
        courseMongo.put("name", "Test Course");
        URL courseMongoURL = new URL(baseAppUrl + "/course?db=mongo");
        response = given().
                contentType(ContentType.JSON).
                body(courseMongo).
        when().
                post(courseMongoURL).
        then().
                statusCode(201).
        extract().
                response();
        body = response.getBody().asString();
        jsonPath = new JsonPath(body);
        mongoTestIds.course = jsonPath.getString("id");

        // we need 120 CAT points worth of modules, but insert 2 extra
        for (int i=0; i<6; ++i) {
            HashMap<String, Object> moduleOracle = new HashMap<>();
            moduleOracle.put("name", "Test Module");
            moduleOracle.put("code", "Test Code " + i);
            moduleOracle.put("semester", "Test Semester");
            moduleOracle.put("catPoints", 30);
            moduleOracle.put("lecturerId", oracleTestIds.lecturer);
            URL moduleOracleURL = new URL(
                    baseAppUrl + "/course/" + oracleTestIds.course + "/modules");
            response = given().
                    contentType(ContentType.JSON).
                    body(moduleOracle).
            when().
                    post(moduleOracleURL).
            then().
                    statusCode(201).
            extract().
                    response();
            body = response.getBody().asString();
            jsonPath = new JsonPath(body);
            if (i<4) {
                String id = jsonPath.getString("id");
                oracleTestIds.module.add(id);
                oracleTestIds.moduleData.put(id, moduleOracle);
                moduleOracle.remove("lecturerId");
                moduleOracle.put("id", id);
                moduleOracle.put("lecturer", "/api/lecturer/" + oracleTestIds.lecturer);
                moduleOracle.put("course", "/api/course/" + oracleTestIds.course);
                moduleOracle.put("studentModules", "/api/course/" +
                        oracleTestIds.course + "/modules/" + id + "/studentModules");
            }

            HashMap<String, Object> moduleMongo = new HashMap<>();
            moduleMongo.put("name", "Test Module");
            moduleMongo.put("code", "Test Code");
            moduleMongo.put("semester", "Test Semester");
            moduleMongo.put("catPoints", 30);
            moduleMongo.put("lecturerId", mongoTestIds.lecturer);
            URL moduleMongoURL = new URL(
                    baseAppUrl + "/course/" + mongoTestIds.course + "/modules?db=mongo");
            response = given().
                    contentType(ContentType.JSON).
                    body(moduleMongo).
            when().
                    post(moduleMongoURL).
            then().
                    statusCode(201).
            extract().
                    response();
            body = response.getBody().asString();
            jsonPath = new JsonPath(body);
            if (i<4) {
                String id = jsonPath.getString("id");
                mongoTestIds.module.add(id);
                mongoTestIds.moduleData.put(id, moduleMongo);
                moduleMongo.remove("lecturerId");
                moduleMongo.put("id", id);
                moduleMongo.put("lecturer", "/api/lecturer/" + mongoTestIds.lecturer);
                moduleMongo.put("course", "/api/course/" + mongoTestIds.course);
                moduleMongo.put("studentModules", "/api/course/" +
                        mongoTestIds.course + "/modules/" + id + "/studentModules");
            }
        }


        for (int oracleOrMongoIndex=0; oracleOrMongoIndex<2; ++oracleOrMongoIndex) {
            List<Map<String, Object>> studentData = oracleOrMongoIndex == 0
                    ? testData.oracle
                    : testData.mongo;
            TestIdContainer testIds = oracleOrMongoIndex == 0
                    ? oracleTestIds
                    : mongoTestIds;

            for (int index=0; index<2; ++index) {
                Map<String, Object> student = new HashMap<>();
                studentData.add(student);

                student.put("firstName", "Test " + index);
                student.put("lastName", "Name " + index);
                student.put("courseId", testIds.course);
            }
        }
    }

    @ParameterizedTest
    @Order(1)
    @ValueSource(strings = {"", "?db=mongo"})
    public void should_register_student(String urlPostfix) throws MalformedURLException {
        URL postUrl = new URL(appUrl + urlPostfix);

        List<Map<String, Object>> studentData = "".equals(urlPostfix)
                ? testData.oracle
                : testData.mongo;
        studentData.forEach(s -> {
            Response response = given().
                    contentType(ContentType.JSON).
                    body(s).
            when().
                    post(postUrl).
            then().
                    statusCode(201).
                    body("firstName", equalTo(s.get("firstName"))).
                    body("lastName", equalTo(s.get("lastName"))).
            extract().
                    response();
            String body = response.getBody().asString();
            JsonPath jsonPath = new JsonPath(body);
            s.put("id", jsonPath.getString("id"));
            s.put("course", jsonPath.getString("course"));
            s.put("modules", jsonPath.getString("modules"));

            // get rid of this, future tests won't see it again
            s.remove("courseId");
        });
    }

    @ParameterizedTest
    @Order(2)
    @ValueSource(strings = {"", "?db=mongo"})
    public void should_get_student(String urlPostfix) {
        List<Map<String, Object>> studentData = "".equals(urlPostfix)
                ? testData.oracle
                : testData.mongo;

        studentData.forEach(s -> {
            URL studentUrl;
            try {
                studentUrl = new URL(appUrl + "/" + s.get("id") + urlPostfix);
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }
            when().
                    get(studentUrl).
            then().
                    statusCode(200).
                    body("$", equalTo(s));
        });
    }

    @ParameterizedTest
    @Order(2)
    @ValueSource(strings = {"", "?db=mongo"})
    public void should_get_all_students(String urlPostfix) throws MalformedURLException {
        List<Map<String, Object>> studentData = "".equals(urlPostfix)
                ? testData.oracle
                : testData.mongo;

        URL studentsUrl = new URL(appUrl + urlPostfix);
        when().
                get(studentsUrl).
        then().
                statusCode(200).
                contentType(ContentType.JSON).
                body("$", equalTo(studentData));
    }

    @ParameterizedTest
    @Order(2)
    @ValueSource(strings = {"", "?db=mongo"})
    public void should_add_student_to_module(String urlPostfix) {
        List<Map<String, Object>> studentData = "".equals(urlPostfix)
                ? testData.oracle
                : testData.mongo;
        TestIdContainer testIds = "".equals(urlPostfix)
                ? oracleTestIds
                : mongoTestIds;

        studentData.forEach(s -> {
            Map<String, Object> body = new HashMap<>();
            body.put("moduleIds", testIds.module.toArray());
            URL studentUrl;
            try {
                studentUrl = new URL(appUrl + "/" + s.get("id") + "/modules" + urlPostfix);
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }
            Response response = given().
                    contentType(ContentType.JSON).
                    body(body).
            when().
                    post(studentUrl).
            then().
                    statusCode(201).
                    contentType(ContentType.JSON).
            extract().
                    response();
            String responseBody = response.getBody().asString();
            JsonPath jsonPath = new JsonPath(responseBody);
            List<String> res = jsonPath.get("moduleIds");
            assertThat(res).containsAll(testIds.module);
        });
    }

    @ParameterizedTest
    @Order(3)
    @ValueSource(strings = {"", "?db=mongo"})
    public void should_submit_module_marks(String urlPostfix) {
        List<Map<String, Object>> studentData = "".equals(urlPostfix)
                ? testData.oracle
                : testData.mongo;
        TestIdContainer testIds = "".equals(urlPostfix)
                ? oracleTestIds
                : mongoTestIds;

        testIds.moduleData.forEach((key, value) -> {
            List<Map<String, Object>> markList = new ArrayList<>();
            testIds.moduleMarks.put(key, markList);
            studentData.forEach(s -> {
                Map<String, Object> mark = new HashMap<>();
                mark.put("studentId", s.get("id"));
                mark.put("mark", 25);
                markList.add(mark);
            });
            URL moduleUrl;
            try {
                moduleUrl = new URL(baseAppUrl + "/lecturer/" + testIds.lecturer + "/modules/" + key + urlPostfix);
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }
            Map<String, Object> payload = new HashMap<>();
            payload.put("marks", markList);
            given().
                    contentType(ContentType.JSON).
                    body(payload).
            when().
                    post(moduleUrl).
            then().
                    statusCode(201).
                    contentType(ContentType.JSON).
                    body("result", equalTo("OK"));
        });

    }

    @ParameterizedTest
    @Order(4)
    @ValueSource(strings = {"", "?db=mongo"})
    public void should_get_students_modules(String urlPostfix) {
        List<Map<String, Object>> studentData = "".equals(urlPostfix)
                ? testData.oracle
                : testData.mongo;
        TestIdContainer testIds = "".equals(urlPostfix)
                ? oracleTestIds
                : mongoTestIds;

        studentData.forEach(s -> testIds.module.forEach(moduleId -> {
            URL studentUrl;
            try {
                studentUrl = new URL(appUrl + "/" + s.get("id") +
                                             "/modules/" + moduleId + urlPostfix);
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }
            when().
                    get(studentUrl).
            then().
                    statusCode(200).
                    contentType(ContentType.JSON).
                    body("module", equalTo(testIds.moduleData.get(moduleId))).
                    body("mark", equalTo((testIds.moduleMarks.get(moduleId).get(0).get("mark"))))
            ;
        }));
    }
}

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
import java.util.stream.Collectors;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;

@Testcontainers
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CourseRESTTest extends BaseIntegrationTest {
    final private static class CourseData {
        final List<Map<String, Object>> moduleData = new ArrayList<>();
        final Map<String, Object> restData = new HashMap<>();
    }

    private static class CoursesTestData {
        final List<CourseData> oracleCourses = new ArrayList<>();
        final List<CourseData> mongoCourses = new ArrayList<>();
    }

    static private URL appUrl;

    static private final CoursesTestData courseTestData = new CoursesTestData();
    CourseRESTTest() {}

    @BeforeAll
    static public void setAppUrl() throws MalformedURLException {
        appUrl = new URL(baseAppUrl + "/course");
    }

    @BeforeAll
    static public void initCourses() throws MalformedURLException {
        // Insert a lecturer for modules
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
        String lecturerIdOracle = jsonPath.getString("id");

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
        String lecturerIdMongo = jsonPath.getString("id");

        for (int oracleOrMongoIndex=0; oracleOrMongoIndex<2; ++oracleOrMongoIndex) {
            List<CourseData> courseData = oracleOrMongoIndex == 0
                    ? courseTestData.oracleCourses
                    : courseTestData.mongoCourses;
            String lecturerId = oracleOrMongoIndex == 0
                    ? lecturerIdOracle
                    : lecturerIdMongo;

            for (int i=2; i<6; i += 2) {
                CourseData course = new CourseData();
                course.restData.put("name", "Course Name: " + i);
                courseData.add(course);

                for (int m=0; m<2; ++m) {
                    Map<String, Object> module = new HashMap<>();
                    module.put("name", "Module Name: " + i + m);
                    module.put("code", "Code: " + i + m);
                    module.put("semester", "Semester: " + i + m);
                    module.put("catPoints", 15);
                    module.put("lecturerId", lecturerId);
                    course.moduleData.add(module);
                }
            }
        }
    }

    @ParameterizedTest
    @Order(1)
    @ValueSource(strings = {"", "?db=mongo"})
    public void should_register_course(String urlPostfix) throws MalformedURLException {
        URL postUrl = new URL(appUrl + urlPostfix);
        List<CourseData> courses = "".equals(urlPostfix)
                ? courseTestData.oracleCourses
                : courseTestData.mongoCourses;
        courses.forEach(c -> {
            Response response = given().
                    contentType(ContentType.JSON).
                    body(c.restData).
            when().
                    post(postUrl).
            then().
                    statusCode(201).
                    body("name", equalTo(c.restData.get("name"))).
            extract().
                    response();
            String body = response.getBody().asString();
            JsonPath jsonPath = new JsonPath(body);
            c.restData.put("id", jsonPath.getString("id"));
            // future responses will have this
            c.restData.put("modules",  jsonPath.getString("modules"));
        });
    }

    @ParameterizedTest
    @Order(2)
    @ValueSource(strings = {"", "?db=mongo"})
    public void should_get_course(String urlPostfix) {
        List<CourseData> courses = "".equals(urlPostfix)
                ? courseTestData.oracleCourses
                : courseTestData.mongoCourses;
        courses.forEach(c -> {
            URL courseUrl;
            try {
                courseUrl = new URL(appUrl + "/" + c.restData.get("id") + urlPostfix);
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }
            when().
                    get(courseUrl).
            then().
                    statusCode(200).
                    contentType(ContentType.JSON).
                    body("id", equalTo(c.restData.get("id"))).
                    body("name", equalTo(c.restData.get("name")));
        });
    }

    @ParameterizedTest
    @Order(2)
    @ValueSource(strings = {"", "?db=mongo"})
    public void should_get_all_courses(String urlPostfix) throws MalformedURLException {
        List<CourseData> courses = "".equals(urlPostfix)
                ? courseTestData.oracleCourses
                : courseTestData.mongoCourses;
        URL postUrl = new URL(appUrl + urlPostfix);
        Response response = when().
                get(postUrl).
        then().
                statusCode(200).
                contentType(ContentType.JSON).
        extract().
                response();

        String body = response.getBody().asString();
        JsonPath jsonPath = new JsonPath(body);
        List<Map<String, Object>> results = jsonPath.getList("$");
        List<Map<String, Object>> mappedData = courses.stream()
                .map(c -> {
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("id", c.restData.get("id"));
                    map.put("name", c.restData.get("name"));
                    map.put("modules", c.restData.get("modules"));
                    return map;
                })
                .collect(Collectors.toList());
        assertThat(results).containsAll(mappedData);
    }

    @ParameterizedTest
    @Order(3)
    @ValueSource(strings = {"", "?db=mongo"})
    public void should_create_modules(String urlPostfix) {
        List<CourseData> courses = "".equals(urlPostfix)
                ? courseTestData.oracleCourses
                : courseTestData.mongoCourses;

        courses.forEach(c -> {
            URL postUrl;
            try {
                postUrl = new URL(baseAppUrl + c.restData.get("modules").toString() + urlPostfix);
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }
            final URL finalPostUrl = postUrl;
            c.moduleData.forEach(m -> {
                Response response = given().
                        contentType(ContentType.JSON).
                        body(m).
                when().
                        post(finalPostUrl).
                then().
                        statusCode(201).
                        body("id", notNullValue()).
                        body("name", equalTo(m.get("name"))).
                        body("code", equalTo(m.get("code"))).
                        body("semester", equalTo(m.get("semester"))).
                        body("catPoints", equalTo(m.get("catPoints"))).
                extract().
                        response();

                String body = response.getBody().asString();
                JsonPath jsonPath = new JsonPath(body);
                String id = jsonPath.getString("id");
                m.put("id", id);
                String lecturer = jsonPath.getString("lecturer");
                m.put("lecturer", lecturer);
                // no longer need this
                m.remove("lecturerId");
            });
        });
    }

    @ParameterizedTest
    @Order(4)
    @ValueSource(strings = {"", "?db=mongo"})
    public void should_get_module(String urlPostfix) {
        List<CourseData> courses = "".equals(urlPostfix)
                ? courseTestData.oracleCourses
                : courseTestData.mongoCourses;
        courses.forEach(c -> c.moduleData.forEach(m -> {
            URL moduleUrl;
            try {
                moduleUrl = new URL(appUrl +
                                            "/" +
                                            c.restData.get("id") +
                                            "/modules/" +
                                            m.get("id") +
                                            urlPostfix);
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }
            when().
                    get(moduleUrl).
            then().
                    statusCode(200).
                    contentType(ContentType.JSON).
                    body("id", equalTo(m.get("id"))).
                    body("name", equalTo(m.get("name"))).
                    body("code", equalTo(m.get("code"))).
                    body("semester", equalTo(m.get("semester"))).
                    body("catPoints", equalTo(m.get("catPoints")));
        }));
    }

    @ParameterizedTest
    @Order(4)
    @ValueSource(strings = {"", "?db=mongo"})
    public void should_get_all_modules(String urlPostfix)  {
        List<CourseData> courses = "".equals(urlPostfix)
                ? courseTestData.oracleCourses
                : courseTestData.mongoCourses;

        courses.forEach(c -> {
            URL moduleUrl;
            try {
                moduleUrl = new URL(baseAppUrl + c.restData.get("modules").toString() + urlPostfix);
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }
            when().
                    get(moduleUrl).
            then().
                    statusCode(200).
                    contentType(ContentType.JSON).
                    body("$", containsInAnyOrder(c.moduleData.toArray()));
        });
    }
}

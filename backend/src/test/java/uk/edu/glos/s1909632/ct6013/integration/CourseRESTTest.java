package uk.edu.glos.s1909632.ct6013.integration;

import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class CourseRESTTest extends BaseIntegrationTest {
    static private URL appUrl;
    static private final List<List<Map<String, String>>> courseList = new ArrayList<>();

    CourseRESTTest() {}

    @BeforeAll
    static public void setAppUrl() throws MalformedURLException {
        appUrl = new URL(baseAppUrl + "course");
    }

    @BeforeAll
    static public void initCourses() {
        courseList.add(new ArrayList<>());
        List<Map<String, String>> courses = courseList.get(0);
        for (int i=0; i<3; ++i) {
            Map<String, String> course = new HashMap<>();
            course.put("name", "Course Name: "+ i);
            courses.add(course);
        }

        courseList.add(new ArrayList<>());
        courses = courseList.get(1);
        for (int i=0; i<3; ++i) {
            Map<String, String> course = new HashMap<>();
            course.put("name", "Course Name: "+ i);
            courses.add(course);
        }
    }

    @ParameterizedTest
    @Order(1)
    @ValueSource(strings = {"", "?db=mongo"})
    public void should_register_course(String urlPostfix) throws MalformedURLException {
        URL postUrl = new URL(appUrl + urlPostfix);
        int courseIndex = "".equals(urlPostfix)
                ? 0
                : 1;
        List<Map<String, String>> courses = courseList.get(courseIndex);
        courses.forEach(c -> {
            Response response = given().
                    contentType(ContentType.JSON).
                    body(c).
            when().
                    post(postUrl).
            then().
                    statusCode(201).
                    body("name", equalTo(c.get("name"))).
            extract().
                    response();
            String body = response.getBody().asString();
            JsonPath jsonPath = new JsonPath(body);
            String id = jsonPath.getString("id");
            c.put("id", id);
            // future responses will have this
            c.put("modules", "/course/" + id + "/modules");
        });
    }

    @ParameterizedTest
    @Order(2)
    @ValueSource(strings = {"", "?db=mongo"})
    public void should_get_course(String urlPostfix) {
        int courseIndex = "".equals(urlPostfix)
                ? 0
                : 1;
        List<Map<String, String>> courses = courseList.get(courseIndex);
        courses.forEach(c -> {
            URL courseUrl;
            try {
                courseUrl = new URL(appUrl + "/" + c.get("id") + urlPostfix);
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }
            when().
                    get(courseUrl).
            then().
                    statusCode(200).
                    contentType(ContentType.JSON).
                    body("id", equalTo(c.get("id"))).
                    body("name", equalTo(c.get("name")));
        });
    }

    @ParameterizedTest
    @Order(2)
    @ValueSource(strings = {"", "?db=mongo"})
    public void should_get_all_courses(String urlPostfix) throws MalformedURLException {
        int courseIndex = "".equals(urlPostfix)
                ? 0
                : 1;
        List<Map<String, String>> courses = courseList.get(courseIndex);
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
        List<Map<String, String>> results = jsonPath.getList("$");
        assertThat(results).containsAll(courses);
    }
}

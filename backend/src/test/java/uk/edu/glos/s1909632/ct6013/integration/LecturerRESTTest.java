package uk.edu.glos.s1909632.ct6013.integration;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.assertj.core.api.Assertions.*;
import static org.hamcrest.Matchers.*;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.testcontainers.junit.jupiter.Testcontainers;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Testcontainers
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class LecturerRESTTest extends BaseIntegrationTest {
    static private URL appUrl;
    static private final List<List<Map<String, String>>> lecturerArray = new ArrayList<>();

    LecturerRESTTest() {
    }

    @BeforeAll
    static public void setAppUrl() throws MalformedURLException {
        appUrl = new URL(baseAppUrl + "lecturer");
    }

    @BeforeAll
    static public void initLecturers() {
        // Do this twice, first for oracle, second for mongo
        lecturerArray.add(new ArrayList<>());
        List<Map<String, String>> lecturers = lecturerArray.get(0);
        for (int i=0; i<3; ++i) {
            Map<String, String> lecturer = new HashMap<>();
            lecturer.put("name", "John Doe: "+ i);
            lecturers.add(lecturer);
        }

        lecturerArray.add(new ArrayList<>());
        lecturers = lecturerArray.get(1);
        for (int i=0; i<3; ++i) {
            Map<String, String> lecturer = new HashMap<>();
            lecturer.put("name", "John Doe: "+ i);
            lecturers.add(lecturer);
        }
    }

    @ParameterizedTest
    @Order(1)
    @ValueSource(strings = {"", "?db=mongo"})
    public void should_register_lecturer(String urlPostfix) throws MalformedURLException {
        URL postUrl = new URL(appUrl + urlPostfix);
        int lecturerIndex = "".equals(urlPostfix)
                ? 0
                : 1;
        List<Map<String, String>> lecturers = lecturerArray.get(lecturerIndex);
        lecturers.forEach(l -> {
            Response response = given().
                            contentType(ContentType.JSON).
                            body(l).
                    when().
                            post(postUrl).
                    then().
                            statusCode(201).
                            body("name", equalTo(l.get("name"))).
                    extract().
                            response();
            String body = response.getBody().asString();
            JsonPath jsonPath = new JsonPath(body);
            String id = jsonPath.getString("id");
            l.put("id", id);
            // future responses will have this
            l.put("modules", "/lecturer/" + id + "/modules");
        });
    }

    @ParameterizedTest
    @Order(2)
    @ValueSource(strings = {"", "?db=mongo"})
    public void should_get_lecturer(String urlPostfix) {
        int lecturerIndex = "".equals(urlPostfix)
                ? 0
                : 1;
        List<Map<String, String>> lecturers = lecturerArray.get(lecturerIndex);
        lecturers.forEach(l -> {
            URL lecturerURL;
            try {
                lecturerURL = new URL(appUrl + "/" + l.get("id") + urlPostfix);
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }
            when().
                    get(lecturerURL).
            then().
                    contentType(ContentType.JSON).
                    body("id", equalTo(l.get("id"))).
                    body("name", equalTo(l.get("name")));
        });
    }

    @ParameterizedTest
    @Order(2)
    @ValueSource(strings = {"", "?db=mongo"})
    public void should_get_all_lecturers(String urlPostfix) throws MalformedURLException {
        int lecturerIndex = "".equals(urlPostfix)
                ? 0
                : 1;
        List<Map<String, String>> lecturers = lecturerArray.get(lecturerIndex);
        URL postUrl = new URL(appUrl + urlPostfix);
        Response response = when().
                get(postUrl).
        then().
                contentType(ContentType.JSON).
        extract().
                response();

        String body = response.getBody().asString();
        JsonPath jsonPath = new JsonPath(body);
        List<Map<String, String>> results = jsonPath.getList("$");
        assertThat(results).containsAll(lecturers);
    }
}

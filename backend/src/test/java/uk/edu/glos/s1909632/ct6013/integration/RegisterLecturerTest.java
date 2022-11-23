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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.containers.Network;
import org.testcontainers.containers.OracleContainer;
import org.testcontainers.containers.output.Slf4jLogConsumer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import org.testcontainers.utility.MountableFile;

import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Testcontainers
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class RegisterLecturerTest {
    static private final String oracleAppUser = "S1909632";
    static private final String getOracleAppUserPass = "mytestpass";
    static private final String mongoDbName = "s1909632";

    static private final Logger logger = LoggerFactory.getLogger(RegisterLecturerTest.class);
    static private final Network net = Network.newNetwork();
    static private final MountableFile warFile = MountableFile.forHostPath(
            Paths.get("target/backend-1.0-SNAPSHOT.war").toAbsolutePath(), 0777
    );
    @Container
    static OracleContainer oracleContainer = new OracleContainer("gvenzl/oracle-xe:21-slim-faststart")
            .withCopyFileToContainer(
                    MountableFile.forHostPath(
                            Paths.get("db-scripts/schema.sql").toAbsolutePath()
                    ),
                    "/container-entrypoint-initdb.d/schema-init.sql"
            )
            .withLogConsumer(new Slf4jLogConsumer(logger))
            .withNetwork(net)
            .withExposedPorts(1521)
            .withUsername(oracleAppUser)
            .withPassword(getOracleAppUserPass);

    @Container
    static MongoDBContainer mongoContainer = new MongoDBContainer(
            DockerImageName.parse("mongo:4.0.10"));

    static GenericContainer appContainer;

    static private URL appUrl;
    static private final List<List<Map<String, String>>> lecturerArray = new ArrayList<>();

    RegisterLecturerTest() {
    }

    @BeforeAll
    static public void initAppContainer() throws MalformedURLException {
        appContainer = new GenericContainer<>("payara/server-web:6.2022.1")
                .withLogConsumer(new Slf4jLogConsumer(logger))
                .withNetwork(net)
                .withExposedPorts(8080)
                .withCopyFileToContainer(warFile, "/opt/payara/deployments/app.war")
                .withEnv("DB_JDBC_URL", oracleContainer.getJdbcUrl())
                .withEnv("DB_JDBC_DRIVER", "oracle.jdbc.pool.OracleDataSource")
                .withEnv("DB_USER", oracleAppUser)
                .withEnv("DB_PASSWORD",  getOracleAppUserPass)
                .withEnv("MONGO_DB_NAME", mongoDbName)
                .withEnv("MONGO_DB_URL", mongoContainer.getConnectionString())
                .waitingFor(Wait.forLogMessage(".* app was successfully deployed .*\\s", 1));
        appContainer.start();
        appUrl = new URL(String.format(
                "http://%s:%s/app/api/lecturer",
                appContainer.getHost(), appContainer.getMappedPort(8080)
        ));
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

    @AfterAll
    static public void stopAppContainer() {
        appContainer.stop();
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
        List<HashMap<String, String>> results = jsonPath.getList("$");
        assertThat(results).allSatisfy(result -> assertThat(lecturers).contains(result));
    }
}

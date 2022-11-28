package uk.edu.glos.s1909632.ct6013.integration;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
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

@Testcontainers
public class BaseIntegrationTest {
    static private final String oracleAppUser = "S1909632";
    static private final String getOracleAppUserPass = "mytestpass";
    static private final String mongoDbName = "s1909632";
    static protected URL baseAppUrl;

    static private final Logger logger = LoggerFactory.getLogger(LecturerRESTTest.class);
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

    BaseIntegrationTest() {}

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
        baseAppUrl = new URL(String.format(
                "http://%s:%s/app/api",
                appContainer.getHost(), appContainer.getMappedPort(8080)
        ));
    }

    @AfterAll
    static public void stopAppContainer() {
        appContainer.stop();
    }
}

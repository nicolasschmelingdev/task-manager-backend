package com.portfolio.taskmanager.support;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class OracleTestContainer {

    private static final int ORACLE_PORT = 1521;

    // gvenzl/oracle-xe image (lightweight) suitable for tests
    public static final GenericContainer<?> ORACLE = new GenericContainer<>("gvenzl/oracle-xe:21-slim")
            .withEnv("ORACLE_PASSWORD", "oracle_password")
            .withExposedPorts(ORACLE_PORT)
            .waitingFor(Wait.forLogMessage(".*DATABASE IS READY TO USE!.*", 1));

    @BeforeAll
    static void startContainer() {
        if (!ORACLE.isRunning()) {
            ORACLE.start();
        }
    }

    @DynamicPropertySource
    static void registerDataSource(DynamicPropertyRegistry registry) {
        String host = ORACLE.getHost();
        Integer port = ORACLE.getMappedPort(ORACLE_PORT);
        String url = String.format("jdbc:oracle:thin:@//%s:%d/XEPDB1", host, port);
        registry.add("spring.datasource.url", () -> url);
        registry.add("spring.datasource.username", () -> "system");
        registry.add("spring.datasource.password", () -> "oracle_password");
        // Keep Flyway enabled to run migrations against the container DB
        registry.add("spring.flyway.enabled", () -> true);
    }
}

package com.amigoscode;


import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.testcontainers.shaded.org.hamcrest.MatcherAssert.assertThat;


@Testcontainers
public class TestContainersTest {

    @Container
    private static PostgreSQLContainer<?> postgreSQLContainer =
            new PostgreSQLContainer<>("postgres:latest")
                    .withDatabaseName("amigoscode-dao-unit-test")
                    .withUsername("amigoscode")
                    .withPassword("password");


    @Test
    public void canStartPostgresDb() {
        System.out.println("host: " + postgreSQLContainer.getHost());
        System.out.println("port: " + postgreSQLContainer.getFirstMappedPort());
        assertThat("postgres sql container running", postgreSQLContainer.isRunning());
        assertThat("postgres sql container is created", postgreSQLContainer.isCreated());
    }
}

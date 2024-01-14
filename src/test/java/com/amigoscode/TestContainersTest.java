package com.amigoscode;


import org.junit.jupiter.api.Test;

import static org.testcontainers.shaded.org.hamcrest.MatcherAssert.assertThat;


public class TestContainersTest extends AbstractTestcontainersTestConfig{

    @Test
    public void canStartPostgresDb() {
        assertThat("postgres sql container running", postgreSQLContainer.isRunning());
        assertThat("postgres sql container is created", postgreSQLContainer.isCreated());
    }
}
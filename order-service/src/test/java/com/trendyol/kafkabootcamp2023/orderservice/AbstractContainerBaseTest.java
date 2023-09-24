package com.trendyol.kafkabootcamp2023.orderservice;

import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

@ContextConfiguration(initializers = {AbstractContainerBaseTest.Initializer.class})
public abstract class AbstractContainerBaseTest {
    static final PostgreSQLContainer POSTGRE_SQL_CONTAINER;
    static {
        POSTGRE_SQL_CONTAINER =  new PostgreSQLContainer<>(DockerImageName
                .parse("registry.trendyol.com/platform/base/image/postgres:11.6")
                .asCompatibleSubstituteFor("postgres"))
                .withDatabaseName("bootcamp-2023")
                .withUsername("bootcamp_2023_appuser")
                .withPassword("bootcamp_2023_apppswd");
        POSTGRE_SQL_CONTAINER.start();
    }
    static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        @Override
        public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
            TestPropertyValues.of("spring.datasource.url=" + POSTGRE_SQL_CONTAINER.getJdbcUrl(),
                    "spring.datasource.username=" + POSTGRE_SQL_CONTAINER.getUsername(),
                    "spring.datasource.password=" + POSTGRE_SQL_CONTAINER.getPassword()
            ).applyTo(configurableApplicationContext.getEnvironment());
        }
    }
}
package ru.clevertec.ecl.spring.integration;

import org.junit.jupiter.api.BeforeAll;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;

@Sql("classpath:db/usersData.sql")
@ActiveProfiles("test")
@Transactional
@SpringBootTest
public abstract class BaseIntegrationTest {

    private static PostgreSQLContainer<?> container = new PostgreSQLContainer<>();

    @BeforeAll
    static void init(){
        container.start();
    }

    @DynamicPropertySource
    static void setUp(DynamicPropertyRegistry registry){
        registry.add("spring.datasource.url", container::getJdbcUrl);
    }
}

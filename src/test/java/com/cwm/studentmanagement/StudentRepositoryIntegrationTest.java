package com.cwm.studentmanagement;

import com.cwm.studentmanagement.model.Students;
import com.cwm.studentmanagement.repository.StudentRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration test demonstrating TestContainers usage with PostgreSQL.
 * This test spins up a containerized Postgres database for integration testing.
 */
@Testcontainers
@DataJpaTest
public class StudentRepositoryIntegrationTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired
    private StudentRepository studentRepository;

    @Test
    public void testSaveAndRetrieveStudent() {
        Students student = new Students();
        student.setFirstName("John");
        student.setLastName("Doe");
        student.setEmail("john@example.com");
        student.setPhoneNumber("1234567890");

        Students saved = studentRepository.save(student);
        assertThat(saved.getId()).isNotNull();

        Students retrieved = studentRepository.findById(saved.getId()).orElse(null);
        assertThat(retrieved).isNotNull();
        assertThat(retrieved.getFirstName()).isEqualTo("John");
        assertThat(retrieved.getLastName()).isEqualTo("Doe");
        assertThat(retrieved.getEmail()).isEqualTo("john@example.com");
    }

    @Test
    public void testActiveStudentsFinder() {
        Students student = new Students();
        student.setFirstName("Jane");
        student.setLastName("Doe");
        student.setEmail("jane@example.com");
        student.setPhoneNumber("9876543210");
        student.setActive(true);
        studentRepository.save(student);

        var activeStudents = studentRepository.findByActiveTrue();
        assertThat(activeStudents).isNotEmpty();
        assertThat(activeStudents.stream()
                .anyMatch(s -> s.getEmail().equals("jane@example.com")))
                .isTrue();
    }

    @Test
    public void testEmailExistenceCheck() {
        Students student = new Students();
        student.setFirstName("Bob");
        student.setLastName("Smith");
        student.setEmail("bob@example.com");
        student.setPhoneNumber("5555555555");
        studentRepository.save(student);

        boolean exists = studentRepository.existsByEmailIgnoreCase("bob@example.com");
        assertThat(exists).isTrue();

        boolean notExists = studentRepository.existsByEmailIgnoreCase("nonexistent@example.com");
        assertThat(notExists).isFalse();
    }
}

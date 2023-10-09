package dev.efullstack.todo.repositories;

import dev.efullstack.todo.models.Todo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.MountableFile;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
@DataR2dbcTest
class TodoRepositoryTest {
    @Container
    @ServiceConnection
    static final PostgreSQLContainer PSQL = new PostgreSQLContainer<>(PostgreSQLContainer.IMAGE)
            .withCopyFileToContainer(MountableFile.forClasspathResource("db/schema.sql"), "/docker-entrypoint-initdb.d/schema.sql")
            .waitingFor(Wait.forListeningPort());

    @Autowired
    private TodoRepository todoRepository;

    @BeforeEach
    void setUp() {
    }

    @Test
    @DisplayName("CRUD Test")
    void crudTest() {
        assertNotNull(todoRepository);
        var newTodo = new Todo(null,"Todo Name", "Todo Description", LocalDateTime.of(2023,10,9,22,10), true);
        assertAll("",
                //Create
                () -> {
                    StepVerifier
                            .create(todoRepository.save(newTodo))
                            .consumeNextWith(r -> {
                                assertNotNull(r);
                                assertNotNull(r.id());
                                assertEquals("Todo Name", r.name());
                                assertEquals("Todo Description", r.description());
                                assertEquals(LocalDateTime.of(2023,10,9,22,10), r.dateTime());
                            })
                            .verifyComplete();
                },
                //Read - all
                () -> {
                    StepVerifier
                            .create(todoRepository.findAll())
                            .expectNextCount(1)
                            .verifyComplete();
                },
                //Read - ById
                () -> {
                    StepVerifier
                            .create(todoRepository.findById("1"))
                            .consumeNextWith(r -> {
                                assertEquals("1", r.id());
                                assertEquals("Todo Name", r.name());
                                assertEquals("Todo Description", r.description());
                                assertEquals(LocalDateTime.of(2023,10,9,22,10), r.dateTime());
                            })
                            .verifyComplete();
                },
                //Update
                () -> {

                },
                //Delete
                () -> {
                });
    }
}
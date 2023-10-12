package dev.efullstack.todo.routers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.MountableFile;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@AutoConfigureWebTestClient
@Testcontainers
class TodoRoutersTest {
    @Container
    @ServiceConnection
    static final PostgreSQLContainer<?> PSQL = new PostgreSQLContainer<>(PostgreSQLContainer.IMAGE)
            .withCopyFileToContainer(MountableFile.forClasspathResource("db/schema.sql"), "/docker-entrypoint-initdb.d/schema.sql")
            .waitingFor(Wait.forListeningPort());

    @Autowired
    WebTestClient webTestClient;

    @Test
    void integrationTest() {
        assertAll(
                () -> {
                    webTestClient
                            .post()
                            .uri("/todo")
                            .contentType(MediaType.APPLICATION_JSON)
                            .bodyValue("""
                                {
                                    "name" : "Car Wash",
                                    "description": "in Toyota",
                                    "dateTime": "2023-10-01T13:20:00",
                                    "status" : true
                                }
                            """)
                            .exchange()
                            .expectStatus()
                            .isCreated()
                            .expectBody()
                            .json("""
                                {
                                    "id": 1,
                                    "name": "Car Wash",
                                    "description": "in Toyota",
                                    "dateTime": "2023-10-01T13:20:00",
                                    "status": true
                                }
                            """);
                },
                () -> {
                    webTestClient
                            .get()
                            .uri("/todo")
                            .exchange()
                            .expectStatus()
                            .isOk()
                            .expectBody()
                            .json("""
                                [
                                    {
                                        "id": 1,
                                        "name": "Car Wash",
                                        "description": "in Toyota",
                                        "dateTime": "2023-10-01T13:20:00",
                                        "status": true
                                    }
                                ]
                            """);
                },
                () -> {
                    webTestClient
                            .get()
                            .uri("/todo/{id}", 1)
                            .exchange()
                            .expectStatus()
                            .isOk()
                            .expectBody()
                            .json("""
                                {
                                    "id": 1,
                                    "name": "Car Wash",
                                    "description": "in Toyota",
                                    "dateTime": "2023-10-01T13:20:00",
                                    "status": true
                                }
                            """);
                },
                () -> {
                    webTestClient
                            .put()
                            .uri("/todo/{id}", 1)
                            .contentType(MediaType.APPLICATION_JSON)
                            .bodyValue("""
                                 {
                                    "id": 1,
                                    "name": "Car Wash Updated",
                                    "description": "in Toyota",
                                    "dateTime": "2023-10-01T13:20:00",
                                    "status": true
                                }
                             """)
                            .exchange()
                            .expectStatus()
                            .isOk()
                            .expectBody()
                            .json("""
                                {
                                    "id": 1,
                                    "name": "Car Wash Updated",
                                    "description": "in Toyota",
                                    "dateTime": "2023-10-01T13:20:00",
                                    "status": true
                                }
                            """);
                },
                () -> {
                    webTestClient
                            .delete()
                            .uri("/todo/{id}", 1)
                            .exchange()
                            .expectStatus()
                            .isOk();

                    webTestClient
                            .get()
                            .uri("/todo/{id}", 1)
                            .exchange()
                            .expectStatus()
                            .isNotFound()
                            .expectBody()
                            .jsonPath("$.path").isEqualTo("/todo/1")
                            .jsonPath("$.status").isEqualTo(404);
                }
        );
    }
}
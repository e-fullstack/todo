package dev.efullstack.todo.routers;

import dev.efullstack.todo.models.Todo;
import dev.efullstack.todo.services.TodoService;
import dev.efullstack.todo.utils.TodoNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Configuration
@RequiredArgsConstructor
public class TodoRouters {
    private final TodoService todoService;

    @Bean
    public RouterFunction routerFunction() {
        return RouterFunctions
                .route()
                .path("/todo", builder -> builder
                        .GET("", this::allTodos)
                        .POST("", this::saveTodo)
                        .GET("/{id}", this::todoById)
                        .PUT("/{id}", this::updateTodo)
                        .DELETE("/{id}", this::deleteTodo)
                )
                .build();
    }

    private Mono<ServerResponse> deleteTodo(ServerRequest request) {
        var id = Long.parseLong(request.pathVariable("id"));
        return todoService.deleteTodo(id)
                .flatMap(ServerResponse.ok()::bodyValue);
    }

    private Mono<ServerResponse> updateTodo(ServerRequest request) {
        var id = Long.parseLong(request.pathVariable("id"));
        return request
                .bodyToMono(Todo.class)
                .flatMap(todo -> todoService.updateTodo(id, todo))
                .flatMap(ServerResponse.ok()::bodyValue);
    }

    private Mono<ServerResponse> todoById(ServerRequest request) {
        var id = Long.parseLong(request.pathVariable("id"));
        return todoService
                .todoById(id)
                .flatMap(ServerResponse.ok()::bodyValue);
    }

    private Mono<ServerResponse> saveTodo(ServerRequest request) {
        return request
                .bodyToMono(Todo.class)
                .flatMap(todoService::newTodo)
                .flatMap(ServerResponse.status(201)::bodyValue);
    }

    private Mono<ServerResponse> allTodos(ServerRequest request) {
        return ServerResponse.ok().body(todoService.allTodo(), Todo.class);
    }
}

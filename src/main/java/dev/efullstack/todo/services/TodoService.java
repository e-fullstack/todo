package dev.efullstack.todo.services;

import dev.efullstack.todo.models.Todo;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface TodoService {
    Flux<Todo> allTodo();
    Mono<Todo> todoById(Long id);
    Mono<Todo> newTodo(Todo todo);
    Mono<Todo> updateTodo(Long id, Todo todo);
    Mono<Void> deleteTodo(Long id);
}

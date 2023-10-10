package dev.efullstack.todo.services;

import dev.efullstack.todo.models.Todo;
import dev.efullstack.todo.repositories.TodoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Service
public class TodoServiceImpl implements TodoService {
    private final TodoRepository todoRepository;

    @Override
    public Flux<Todo> allTodo() {
        return todoRepository.findAll();
    }

    @Override
    public Mono<Todo> todoById(Long id) {
        return todoRepository.findById(id);
    }

    @Override
    public Mono<Todo> newTodo(Todo todo) {
        return todoRepository.save(todo);
    }

    @Override
    public Mono<Todo> updateTodo(Long id, Todo todo) {
        return todoById(id)
                //TODO fix this NotFound scenario
                .map(todo1 -> todo)
                .flatMap(todoRepository::save);
    }

    @Override
    public Mono<Void> deleteTodo(Long id) {
        return todoById(id)
                //TODO fix this NotFound scenario
                .map(Todo::id)
                .flatMap(todoRepository::deleteById);
    }
}

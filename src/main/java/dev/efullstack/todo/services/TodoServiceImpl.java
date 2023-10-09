package dev.efullstack.todo.services;

import dev.efullstack.todo.repositories.TodoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class TodoServiceImpl implements TodoService {
    private final TodoRepository todoRepository;

}

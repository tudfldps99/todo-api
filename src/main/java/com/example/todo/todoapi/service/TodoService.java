// 2023-01-18
package com.example.todo.todoapi.service;

import com.example.todo.todoapi.entity.TodoEntity;
import com.example.todo.todoapi.repository.TodoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class TodoService {      // 중간처리
    private final TodoRepository todoRepository;

    // 할 일 목록 조회
    public List<TodoEntity> retrieve() {
        return todoRepository.findAll();
    }
}

// TODO: 2023-01-18
package com.example.todo.todoapi.repository;

import com.example.todo.todoapi.entity.TodoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface TodoRepository extends JpaRepository<TodoEntity, String> {

    // 완료되지 않은 할일들만 조회
    @Query("SELECT t FROM TodoEntity t WHERE t.done=1")
    List<TodoEntity> findNotYetTodos();
}

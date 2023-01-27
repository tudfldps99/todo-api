// TODO: 2023-01-18
package com.example.todo.todoapi.repository;

import com.example.todo.todoapi.entity.TodoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface TodoRepository extends JpaRepository<TodoEntity, String> {

    // 완료되지 않은 할일들만 조회
    @Query("SELECT t FROM TodoEntity t WHERE t.done=0")
    List<TodoEntity> findNotYetTodos();

    // 2023-01-27
    // 특정 회원의 할일 목록 조회
    List<TodoEntity> findByUserId(String userId);
    /*
        @Query("select t from TodoEntity t where t.user.id=:userId")
        List<TodoEntity> findByUser(@Param(value = "userId") String user_id);
     */
}

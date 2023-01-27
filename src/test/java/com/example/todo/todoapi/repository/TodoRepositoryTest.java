// 2023-01-18
package com.example.todo.todoapi.repository;

import com.example.todo.todoapi.entity.TodoEntity;
import com.example.todo.userapi.entity.UserEntity;
import com.example.todo.userapi.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
// @Commit // test 실행 후 커밋
class TodoRepositoryTest {

    @Autowired TodoRepository todoRepository;
    @Autowired UserRepository userRepository;   // 2023-01-27

    // 2023-01-27 (주석처리)
//    @BeforeEach
//    void insertTest() {
//        TodoEntity todo1 = TodoEntity.builder().title("저녁 장보기").build();
//        TodoEntity todo2 = TodoEntity.builder().title("책 읽기").build();
//        TodoEntity todo3 = TodoEntity.builder().title("목욕하기").build();
//
//        todoRepository.save(todo1);
//        todoRepository.save(todo2);
//        todoRepository.save(todo3);
//    }

    @Test
    @DisplayName("할 일 목록을 조회하면 리스트의 사이즈가 3이어야 한다.")
    void findAllTest() {
        //given
        //when
        List<TodoEntity> list = todoRepository.findAll();
        //then
        assertEquals(3, list.size());
    }

    // 2023-01-27
    @Test
    @DisplayName("회원의 할 일을 등록해야 한다.")
    void saveTodoWithUserTest() {
        UserEntity user = userRepository.findByEmail("test2@naver.com");

        // given
        TodoEntity todo = TodoEntity.builder()
                .title("택배 찾으러가기")
                .user(user)
                .build();

        // when
        TodoEntity saveTodo = todoRepository.save(todo);

        // then
        assertEquals(saveTodo.getUser().getId(), user.getId());
    }

    @Test
    @DisplayName("특정 회원의 할일 목록을 조회해야 한다.")
    @Transactional
    void findByUserTest() {
        // given
        String userId = "402880e785f11a290185f12251150000";

        // when
        List<TodoEntity> todos = todoRepository.findByUserId(userId);

        // then
        todos.forEach(System.out::println);
        assertEquals(2, todos.size());
    }
}
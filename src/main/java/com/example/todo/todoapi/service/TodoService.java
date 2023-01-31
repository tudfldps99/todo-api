// 2023-01-18
package com.example.todo.todoapi.service;

import com.example.todo.todoapi.dto.request.TodoCreateRequestDTO;
import com.example.todo.todoapi.dto.request.TodoModifyRequestDTO;
import com.example.todo.todoapi.dto.response.TodoDetailResponseDTO;
import com.example.todo.todoapi.dto.response.TodoListResponseDTO;
import com.example.todo.todoapi.entity.TodoEntity;
import com.example.todo.todoapi.repository.TodoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class TodoService {      // 중간처리
    private final TodoRepository todoRepository;

    // 할 일 목록 조회
    @Transactional
    public TodoListResponseDTO retrieve(String userId) {        // 2023-01-27) findByUserId로 변경하면서 retrieve() 에 매개변수로 String userId 추가
        //List<TodoEntity> entityList = todoRepository.findAll();     // -> TodoEntity 를 TodoListResponseDTO 로 변경해서 return 해줘야 함

        // 2023-01-27) findAll 이 아닌 findByUserId 로 변경 - 해당 user 의 할 일만 받아와야 하기 때문
        List<TodoEntity> entityList = todoRepository.findByUserId(userId);

        // 1. TodoEntity 를 TodoDetailResponseDTO 로 우선 변경
        List<TodoDetailResponseDTO> dtoList = entityList.stream()
                .map(TodoDetailResponseDTO::new)
                .collect(Collectors.toList());

        // 2. TodoDetailResponseDTO 를 TodoListResponseDTO 로 변경
        return TodoListResponseDTO.builder()
                .todos(dtoList)
                .build();
    }

    // 할 일 등록
    public TodoListResponseDTO create(
            final TodoCreateRequestDTO createRequestDTO,
            final String userId
    )
        throws RuntimeException
    {
        TodoEntity todo = createRequestDTO.toEntity();      // createRequestDTO 를 entity 로 변환
         // user 정보를 직접 추가해줘야함. like) todo.setUser(Entity 값)
        // why? createRequestDTO 에는 user 정보가 없기때문 (TodoEntity 에서 title은 DTO가 보내고, todoId, done, createDate는 자동이지만, user만 없음)
        // but) 그러기 위해서는 user를 SELECT 한 후 id값을 넣어줌? SELECT 할게 1000개라면?
        // -> TodoEntity.java 에 INSERT,UPDATE 시에만 사용 할 userId 생성
        // => userRepository 에 의존할 필요가 없어짐
        todo.setUserId(userId);

        todoRepository.save(todo);      // DTO 를 Entity 로 변환해서 save 메서드 안에 넣기 --> TodoCreateRequestDTO.java 참고
        log.info("할 일이 저장되었습니다. 제목 : {}", createRequestDTO.getTitle());
        return retrieve(userId);
    }

    // 할 일 수정 (제목, 할 일 완료여부)
    public TodoListResponseDTO update(
            final String id,        // id : 수정 대상의 id
            final TodoModifyRequestDTO modifyRequestDTO,        // DTO : 클라이언트가 보낸 데이터
            final String userId
    ) {

        // 수정 target 조회
        Optional<TodoEntity> targetEntity = todoRepository.findById(id);

        targetEntity.ifPresent(entity -> {      // targetEntity 가 만약 존재한다면 dto 의 값을 이용하여 setter 로 수정. 이때 dto 는 클라이언트가 보낸 데이터
            entity.setTitle(modifyRequestDTO.getTitle());
            entity.setDone(modifyRequestDTO.isDone());

            todoRepository.save(entity);        // setter 해준 후 다시 save
        });

        return retrieve(userId);
    }

    // 할 일 삭제
    public TodoListResponseDTO delete(final String id, final String userId) {        // id : 삭제 대상의 id
        try {
           todoRepository.deleteById(id);       // id가 존재하지 않거나 null 이면 error
        } catch (Exception e) {
            log.error("id가 존재하지 않아 삭제에 실패했습니다. - ID: {}, err: {}", id, e.getMessage());     // [서버에 기록할 메세지]

            throw new RuntimeException("id가 존재하지 않아 삭제에 실패했습니다.");      // [클라이언트에게 전달할 메세지]
        }
        return retrieve(userId);
    }
}

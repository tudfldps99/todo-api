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

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class TodoService {      // 중간처리
    private final TodoRepository todoRepository;

    // 할 일 목록 조회
    public TodoListResponseDTO retrieve() {
        List<TodoEntity> entityList = todoRepository.findAll();     // -> TodoEntity 를 TodoListResponseDTO 로 변경해서 return 해줘야 함

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
    public TodoListResponseDTO create(final TodoCreateRequestDTO createRequestDTO) {
        todoRepository.save(createRequestDTO.toEntity());      // DTO 를 Entity 로 변환해서 save 메서드 안에 넣기 --> TodoCreateRequestDTO.java 참고
        log.info("할 일이 저장되었습니다. 제목 : {}", createRequestDTO.getTitle());
        return retrieve();
    }

    // 할 일 수정 (제목, 할 일 완료여부)
    public TodoListResponseDTO update(final String id, final TodoModifyRequestDTO modifyRequestDTO) {       // id : 수정 대상의 id, DTO : 클라이언트가 보낸 데이터

        // 수정 target 조회
        Optional<TodoEntity> targetEntity = todoRepository.findById(id);

        targetEntity.ifPresent(entity -> {      // targetEntity 가 만약 존재한다면 dto 의 값을 이용하여 setter 로 수정. 이때 dto 는 클라이언트가 보낸 데이터
            entity.setTitle(modifyRequestDTO.getTitle());
            entity.setDone(modifyRequestDTO.isDone());

            todoRepository.save(entity);        // setter 해준 후 다시 save
        });

        return retrieve();
    }

    // 할 일 삭제
    public TodoListResponseDTO delete(final String id) {        // id : 삭제 대상의 id
        try {
           todoRepository.deleteById(id);       // id가 존재하지 않거나 null 이면 error
        } catch (Exception e) {
            log.error("id가 존재하지 않아 삭제에 실패했습니다. - ID: {}, err: {}", id, e.getMessage());     // [서버에 기록할 메세지]

            throw new RuntimeException("id가 존재하지 않아 삭제에 실패했습니다.");      // [클라이언트에게 전달할 메세지]
        }
        return retrieve();
    }
}

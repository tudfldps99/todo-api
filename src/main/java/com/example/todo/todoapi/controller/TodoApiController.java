// TODO: 2023-01-18
package com.example.todo.todoapi.controller;

import com.example.todo.todoapi.dto.request.TodoCreateRequestDTO;
import com.example.todo.todoapi.dto.request.TodoModifyRequestDTO;
import com.example.todo.todoapi.dto.response.TodoListResponseDTO;
import com.example.todo.todoapi.service.TodoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/todos")
// 2023-01-20) CORS 허용 설정
@CrossOrigin(origins = "*" )       // client 웹의 url
public class TodoApiController {
    private final TodoService todoService;

    // 할 일 등록 요청
    @PostMapping
    public ResponseEntity<?> createTodo(
            @Validated @RequestBody TodoCreateRequestDTO requestDTO
            , BindingResult result
    ) {
        if (result.hasErrors()) {       // 검증 에러 발생하면 badRequest
            log.warn("DTO 검증 에러 발생 : {}", result.getFieldError());
            return ResponseEntity
                    .badRequest()
                    .body(result.getFieldError());
        }
        try {       // ctrl + alt + t
            TodoListResponseDTO responseDTO = todoService.create(requestDTO);
            return ResponseEntity
                    .ok()
                    .body(responseDTO);
        } catch (RuntimeException e) {
            log.error(e.getMessage());
            return ResponseEntity
                    .internalServerError()
                    .body(TodoListResponseDTO.builder().error(e.getMessage()));     // TodoListResponseDTO.java 의 error 에 담김
        }
    }

    // 할 일 삭제 요청
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTodo(@PathVariable("id") String todoId) {        // id를 경로에서 읽음
        log.info("/api/todos/{} DELETE request!", todoId);

        if (todoId == null || todoId.equals("")) {
            return ResponseEntity
                    .badRequest()
                    .body(TodoListResponseDTO.builder().error("ID를 전달해주세요"));
        }

        try {       // ctrl + alt + t
            TodoListResponseDTO responseDTO = todoService.delete(todoId);
            return ResponseEntity
                    .ok()
                    .body(responseDTO);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity
                    .internalServerError()
                    .body(TodoListResponseDTO.builder().error(e.getMessage()));     // e.getMessage() 는 TodoService.java 에서 delete 메소드의 throw new RuntimeException(); 를 의미
        }
    }

    // 할 일 목록요청 (GET)
    @GetMapping
    public ResponseEntity<?> listTodo() {

        log.info("/api/todos GET request!");

        TodoListResponseDTO responseDTO = todoService.retrieve();

        return ResponseEntity.ok().body(responseDTO);
    }

    // 할 일 수정요청 (PUT, PATCH)
    @RequestMapping( value = "/{id}"
            , method = {RequestMethod.PUT, RequestMethod.PATCH}
    )
    public ResponseEntity<?> updateTodo(
            @PathVariable("id") String todoId
            , @Validated @RequestBody TodoModifyRequestDTO requestDTO
            , BindingResult result
            , HttpServletRequest request        // PUT 인지, PATCH 인지 요청정보를 알 수 있음

    ) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest()
                    .body(result.getFieldError());
        }

        log.info("/api/todos/{} {} request", todoId, request.getMethod());
        log.info("modifying dto : {}", requestDTO);

        try {
            TodoListResponseDTO responseDTO = todoService.update(todoId, requestDTO);
            return ResponseEntity.ok().body(responseDTO);
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(TodoListResponseDTO.builder().error(e.getMessage()));
        }
    }
}
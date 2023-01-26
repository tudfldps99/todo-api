// 2023-01-26
package com.example.todo.userapi.exception;

// 사용자 정의 예외 클래스 : 이메일 중복 에러

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class DuplicatedEmailException extends RuntimeException {

    public DuplicatedEmailException(String message) {
        super(message);
    }
}
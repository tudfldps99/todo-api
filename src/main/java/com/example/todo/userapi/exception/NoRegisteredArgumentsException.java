// 2023-01-26
package com.example.todo.userapi.exception;

// 사용자 정의 예외 클래스 : 가입정보 없는 에러

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class NoRegisteredArgumentsException extends RuntimeException {

    // 기본 생성자 -> lombok : @NoArgsConstructor

    // 에러 메시지를 처리하는 생성자
    public NoRegisteredArgumentsException(String message) {
        super(message);
    }
}

// 2023-01-27
package com.example.todo.userapi.dto;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

// 로그인 시 클라이언트가 보낸 데이터를 담는 객체 (사용자가 입력하는 값들)

@Setter @Getter @ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
public class LoginRequestDTO {

    @Email
    @NotBlank
    private String email;

    @NotBlank
    @Size(min = 8, max = 20, message = "잘못된 비밀번호입니다.")
    private String password;
}

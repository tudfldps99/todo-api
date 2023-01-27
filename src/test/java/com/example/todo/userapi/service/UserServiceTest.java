// 2023-01-26
package com.example.todo.userapi.service;

import com.example.todo.userapi.dto.UserSignUpDTO;
import com.example.todo.userapi.dto.UserSignUpResponseDTO;
import com.example.todo.userapi.entity.UserEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserServiceTest {

    @Autowired UserService userService;

    @Test
    @DisplayName("중복된 이메일이 포함된 회원정보로 가입하면 RuntimeException 이 발생해야 한다.")
    void validateEmailTest() {      // 의도적인 실패 테스트
        // given
        UserSignUpDTO dto = UserSignUpDTO.builder()
                .email("abc1234@def.com")
                .password("545454")
                .userName("키키킥")
                .build();

        // when
        //userService.create(dto);      --> "중복된 이메일입니다"

        // then
        assertThrows(RuntimeException.class, () -> {
            userService.create(dto);        // --> 에러가 발생할 거라고 단언 했으므로 테스트 성공
        });
    }

    @Test
    @DisplayName("검증된 회원정보로 가입하면 회원가입에 성공해야 한다.")
    void createTest() {
        // given
        UserSignUpDTO dto = UserSignUpDTO.builder()
                .email("tututut@def.com")
                .password("5678")
                .userName("암호맨")
                .build();

        // when
        UserSignUpResponseDTO responseDTO = userService.create(dto);

        // then
        System.out.println("responseDTO = " + responseDTO);
        assertEquals("암호맨", responseDTO.getUserName());
    }

    // 2023-01-27
    @Test
    @DisplayName("존재하지 않는 이메일로 로그인을 시도하면 Exception이 발생해야 한다.")
    void noUseTest() {
        // given
        String email = "alkjadfkaj@sss.com";        // 없는 이메일
        String password = "1234";

        // when

        // then
        assertThrows(RuntimeException.class, () -> {
            //when
            userService.getByCredentials(email, password);
        });
    }

    @Test
    @DisplayName("틀린 비밀번호로 로그인을 시도하면 Exception이 발생해야 한다.")
    void invalidPasswordTest() {
        // given
        String email = "postman@naver.com";
        String password = "dpqpqp12!";       // 틀린 비밀번호

        // when

        // then
        assertThrows(RuntimeException.class, () -> {
            //when
            userService.getByCredentials(email, password);
        });
    }

    @Test
    @DisplayName("정확한 정보로 로그인을 시도하면 회원정보가 반환되어야 한다.")
    void loginTest() {
        // given
        String email = "test@naver.com";
        String password = "test12!@";

        // when
        UserEntity loginUser = userService.getByCredentials(email, password);

        // then
        assertEquals("테스트", loginUser.getUserName());
    }
}
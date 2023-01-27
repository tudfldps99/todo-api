// 2023-01-26
package com.example.todo.userapi.service;

import com.example.todo.userapi.dto.UserSignUpDTO;
import com.example.todo.userapi.dto.UserSignUpResponseDTO;
import com.example.todo.userapi.entity.UserEntity;
import com.example.todo.userapi.exception.DuplicatedEmailException;
import com.example.todo.userapi.exception.NoRegisteredArgumentsException;
import com.example.todo.userapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;      // final 로 생성하면 @RequiredArgsConstructor 를 이용하여 자동으로 생성자 주입됨

    // 회원가입 처리
    public UserSignUpResponseDTO create(final UserSignUpDTO userSignUpDTO) {
        if (userSignUpDTO == null) {
            //throw new RuntimeException("가입정보가 없습니다.");        // -> NoRegisteredArgumentsException.java 생성
            throw new NoRegisteredArgumentsException("가입정보가 없습니다.");
        }
        final String email = userSignUpDTO.getEmail();
        if (userRepository.existsByEmail(email)) {      // 이메일 중복검사     --> 누군가 클라이언트의 요청을 피해서 중복확인을 했을 때 검사    => 이메일 중복확인은 따로 진행 (하단)
            log.warn("Email already exists - {}", email);
            //throw new RuntimeException("중복된 이메일입니다.");    // -> DuplicatedEmailException.java 생성
            throw new DuplicatedEmailException("중복된 이메일입니다.");
        }

        // 패스워드 인코딩
        String rawPassword = userSignUpDTO.getPassword();   // 평문 패스워드
        String encodedPassword = passwordEncoder.encode(rawPassword);   // 암호화처리
        userSignUpDTO.setPassword(encodedPassword);     // 암호화된 패스워드 set

        // 회원가입
        UserEntity savedUser = userRepository.save(userSignUpDTO.toEntity());    // DTO 를 Entity 로 변환시킴

        log.info("회원 가입 성공!! - user_id : {}", savedUser.getId());

        // savedUser 를 그대로 return 하면 클라이언트에게 비밀번호 등 모두 보여주는 것이므로 UserSignUpResponseDTO 에서 dto 로 변환시켜서 return
        return new UserSignUpResponseDTO(savedUser);
    }

    // 이메일 중복확인
    public boolean isDuplicate(String email) {
        if (email == null) {
            throw new RuntimeException("이메일 값이 없습니다.");
        }
        return userRepository.existsByEmail(email);
    }

    // 2023-01-27
    // 로그인 검증
    public UserEntity getByCredentials(final String email, final String rawPassword) {

        // 입력한 이메일을 통해 회원정보 조회
        UserEntity originalUser = userRepository.findByEmail(email);
        if (originalUser == null) {        // 가입 X
            throw new RuntimeException("가입된 회원이 아닙니다.");
        }

        // 패스워드 검증 (입력 비번과 DB에 저장된 비번 대조)
        if (!passwordEncoder.matches(rawPassword, originalUser.getPassword())) {        // !입력비번.equals(DB비번)  =  !passwordEncoder.matches(입력비번, DB비번)
            throw new RuntimeException("비밀번호가 틀렸습니다.");
        }
        log.info("{}님 로그인 성공!", originalUser.getUserName());

        return originalUser;        // 로그인 한 회원의 정보를 return
    }
}

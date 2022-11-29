package com.example.hospitalreview.service;

import com.example.hospitalreview.domain.User;
import com.example.hospitalreview.domain.UserDto;
import com.example.hospitalreview.domain.dto.UserJoinRequest;
import com.example.hospitalreview.exception.ErrorCode;
import com.example.hospitalreview.exception.HospitalReviewAppException;
import com.example.hospitalreview.repository.UserRepository;
import com.example.hospitalreview.utils.JwtTokenUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder;
    @Value("${jwt.token.secret}") // spring에서 지원하는 어쩌고
    private String secretKey;
    private long expireTimeMs = 1000 * 60 * 60; // 1초 * 60 * 60 = 1hour

    public UserDto join(UserJoinRequest request) {
        // 비즈니스 로직 - 회원 가입

        // 회원 userName(id) 중복 check

        // 중복되면 회원가입 불가능 exception발생
        userRepository.findByUserName(request.getUserName())
                .ifPresent(user->{
                    throw new HospitalReviewAppException(ErrorCode.DUPLICATED_USER_NAME,
                            String.format("username : %s", request.getUserName()));
                });

        // 회원가입 .save()
        User savedUser = userRepository.save(request.toEntity(encoder.encode(request.getPassword())));
        return UserDto.builder()
                .id(savedUser.getId())
                .userName(savedUser.getUserName())
                .email(savedUser.getEmailAddress())
                .build();
    }

    public String login(String userName, String password) {
        //userName 있는지 여부 확인
        // 없다면 NOT-FOUND 에러
        User user = userRepository.findByUserName(userName)
                .orElseThrow(()->new HospitalReviewAppException(ErrorCode.NOT_FOUND,
                        String.format("%s는 가입된 적이 없습니다.",userName)));
        // password 일치 하는지 확인
        if(!encoder.matches(password, user.getPassword())){
            throw new HospitalReviewAppException(ErrorCode.INVALID_PASSWORD, String.format(("userName 또는 password가 잘못되었습니다.")));
        }
        // 두 가지 확인 도중 예외가 안났다면 token 발행
        return JwtTokenUtils.createToken(userName,secretKey,expireTimeMs);
    }
}

package com.example.hospitalreview.service;

import com.example.hospitalreview.domain.User;
import com.example.hospitalreview.domain.UserDto;
import com.example.hospitalreview.domain.dto.UserJoinRequest;
import com.example.hospitalreview.exception.ErrorCode;
import com.example.hospitalreview.exception.HospitalReviewAppException;
import com.example.hospitalreview.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder;
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
}

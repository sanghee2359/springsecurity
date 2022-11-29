package com.example.hospitalreview.controller;

import com.example.hospitalreview.domain.Response;
import com.example.hospitalreview.domain.UserDto;
import com.example.hospitalreview.domain.dto.UserJoinRequest;
import com.example.hospitalreview.domain.dto.UserJoinResponse;
import com.example.hospitalreview.domain.dto.UserLoginRequest;
import com.example.hospitalreview.domain.dto.UserLoginResponse;
import com.example.hospitalreview.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    @PostMapping("/join")
    public Response<UserJoinResponse> join(@RequestBody UserJoinRequest userJoinRequest) {
        UserDto userDto = userService.join(userJoinRequest);
        return Response.success(new UserJoinResponse(userDto.getUserName(), userDto.getEmail()));
    }
    @PostMapping("/login")
    public Response<UserLoginResponse> join(@RequestBody UserLoginRequest userLoginRequest) {
        String token = userService.login(userLoginRequest.getUserName(),userLoginRequest.getPassword());
        return Response.success(new UserLoginResponse(token));
    }
}

package com.example.hospitalreview.controller;

import com.example.hospitalreview.domain.UserDto;
import com.example.hospitalreview.domain.dto.UserJoinRequest;
import com.example.hospitalreview.exception.ErrorCode;
import com.example.hospitalreview.exception.HospitalReviewAppException;
import com.example.hospitalreview.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// controller 테스트 -> 비즈니스 로직이 존재하면 service 테스트 역시 만들어야함
@WebMvcTest
class UserControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;
    @MockBean
    UserService userService;

    @Test
    @DisplayName("회원가입 성공")
    @WithMockUser
    void join_success() throws  Exception {
        UserJoinRequest userJoinRequest = UserJoinRequest.builder()
                .userName("sanghee")
                .password("sanghee065")
                .email("sangheeEmail.com")
                .build();

        when(userService.join(any())).thenReturn(mock(UserDto.class));
        mockMvc.perform(post("/api/v1/users/join").with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(userJoinRequest)))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("회원가입 실패-id 중복")
    void join_fail() throws  Exception {
        UserJoinRequest userJoinRequest = UserJoinRequest.builder()
                .userName("sanghee")
                .password("sanghee065")
                .email("sangheeEmail.com")
                .build();
        when(userService.join(any())).thenThrow(new HospitalReviewAppException(ErrorCode.DUPLICATED_USER_NAME, ""));

        mockMvc.perform(post("/api/v1/users/join").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(userJoinRequest)))
                .andDo(print())
                .andExpect(status().isConflict());
    }
}
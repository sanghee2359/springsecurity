package com.example.hospitalreview.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class Response<T> {
    private String resultCode;
    private T result; // 모든 결과 페이지는 Response객체로 감싸서 리턴

    public static <T> Response<T> success(T result) {
        return new Response("SUCCESS", result);
    }
    public static Response<Void> error(String resultCode){
        return new Response(resultCode, null);


    }
}

package com.week7.bannybannycarrotcarrot.errorcode;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum CommonStatusCode implements StatusCode {

    INVALID_PARAMETER("Invalid parameter included", HttpStatus.BAD_REQUEST.value()),
    NOT_FIND_POST("게시글을 찾을 수 없습니다.", HttpStatus.BAD_REQUEST.value()),
    NOT_FIND_USER("회원을 찾을 수 없습니다.", HttpStatus.BAD_REQUEST.value());







    private final String msg;
    private final int statusCode;
}

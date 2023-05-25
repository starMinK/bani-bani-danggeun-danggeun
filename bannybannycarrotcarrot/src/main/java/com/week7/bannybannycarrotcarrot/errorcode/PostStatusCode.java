package com.week7.bannybannycarrotcarrot.errorcode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum PostStatusCode implements StatusCode{
    CREATE_POST("게시글 작성 완료.", HttpStatus.OK.value()),
    UPDATE_POST("게시글 수정 성공", HttpStatus.OK.value()),
    DELETE_POST("게시글 삭제 성공", HttpStatus.OK.value()),
    INVALID_USER_UPDATE("작성자만 수정할 수 있습니다.", HttpStatus.BAD_REQUEST.value()),
    INVALID_USER_DELETE("작성자만 삭제할 수 있습니다.", HttpStatus.BAD_REQUEST.value());



    private final String msg;
    private final int statusCode;
}

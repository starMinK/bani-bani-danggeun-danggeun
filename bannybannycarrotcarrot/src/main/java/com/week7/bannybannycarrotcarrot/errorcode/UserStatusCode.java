package com.week7.bannybannycarrotcarrot.errorcode;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum UserStatusCode implements StatusCode{
    USER_SIGNUP_SUCCESS("회원가입 성공", HttpStatus.OK.value()),
    USER_LOGIN_SUCCESS("로그인 성공", HttpStatus.OK.value()),
    WRONG_USERNAME_PATTERN("아이디는 최소 5자 이상, 10자 이하이며 알파벳 소문자(a~z), 숫자(0~9)로 구성되어야 합니다.", HttpStatus.BAD_REQUEST.value()),
    WRONG_USERNAME_SIZE("아이디는 최소 5자 이상, 10자 이하로 구성되어야 합니다.", HttpStatus.BAD_REQUEST.value()),
    WRONG_USERNAME_PATTERN2("아이디는 알파벳 소문자(a~z), 숫자(0~9)로 구성되어야 합니다.", HttpStatus.BAD_REQUEST.value()),
    WRONG_PASSWORD_PATTERN("비밀번호는 최소 8자 이상, 15자 이하이며 알파벳 대소문자(a~z, A~Z), 숫자(0~9), 특수문자로 구성되어야 합니다.", HttpStatus.BAD_REQUEST.value()),
    OVERLAPPED_USERNAME("이미 사용중인 아이디입니다.", HttpStatus.BAD_REQUEST.value()),
    AVAILABLE_USERNAME("사용 가능한 아이디입니다.", HttpStatus.OK.value()),

    AVAILABLE_NICKNAME("사용 가능한 닉네임입니다.", HttpStatus.OK.value()),
    OVERLAPPED_NICKNAME("중복된 닉네임 입니다.", HttpStatus.BAD_REQUEST.value()),
    PASSWORD_CHECK("입력된 비밀번호가 다릅니다.", HttpStatus.BAD_REQUEST.value());


    private final String msg;
    private final int statusCode;
}

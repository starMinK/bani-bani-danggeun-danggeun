package com.week7.bannybannycarrotcarrot.errorcode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ChatStatusCode implements StatusCode{
    NO_ARRAY_EXCEPTION("참여하고 있는 채팅방이 존재하지 않습니다.", HttpStatus.OK.value());

    private final String msg;
    private final int statusCode;

    }

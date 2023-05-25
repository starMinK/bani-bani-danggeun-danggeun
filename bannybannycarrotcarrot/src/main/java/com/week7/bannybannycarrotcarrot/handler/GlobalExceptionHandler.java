package com.week7.bannybannycarrotcarrot.handler;

import com.week7.bannybannycarrotcarrot.dto.ErrorDto;
import com.week7.bannybannycarrotcarrot.dto.ErrorResponseDto;
import com.week7.bannybannycarrotcarrot.errorcode.CommonStatusCode;
import com.week7.bannybannycarrotcarrot.errorcode.StatusCode;
import com.week7.bannybannycarrotcarrot.errorcode.UserStatusCode;
import com.week7.bannybannycarrotcarrot.exception.RestApiException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {


    // RestApiException 에러 핸들링
    @ExceptionHandler(RestApiException.class)
    public ResponseEntity<Object> handleCustomException(RestApiException e) {

        StatusCode statusCode = e.getStatusCode();
        return handleExceptionInternal(statusCode);
    }

    // MethodArgumentNotValid 에러 핸들링
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException e,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request) {
        log.warn("handleMethodArgumentNotValid", e);
        String errorFieldName = e.getBindingResult().getFieldError().getField();
        StatusCode statusCode = CommonStatusCode.INVALID_PARAMETER;
        if(errorFieldName.equals("username")){
            statusCode = UserStatusCode.WRONG_USERNAME_PATTERN;
        }else if(errorFieldName.equals("password")){
            statusCode = UserStatusCode.WRONG_PASSWORD_PATTERN;
        }
        return handleExceptionInternal(statusCode);
    }






    // ErrorCode 만 있는 에러 ResponseEntity 생성
    private ResponseEntity<Object> handleExceptionInternal(StatusCode statusCode) {
        return ResponseEntity.status(statusCode.getStatusCode())
                // ErrorCode 만 있는 에러 responseEntity body만들기
                .body(makeErrorResponse(statusCode));
    }
    private ErrorDto makeErrorResponse(StatusCode statusCode) {
        return ErrorDto.builder()
                .statusCode(statusCode.getStatusCode())
                .msg(statusCode.getMsg())
                .build();
    }



}

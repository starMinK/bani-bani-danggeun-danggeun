package com.week7.bannybannycarrotcarrot.security.exceptionhandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;

import com.week7.bannybannycarrotcarrot.dto.ErrorResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RequiredArgsConstructor
@Slf4j
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    private final ObjectMapper om;

    private static final String CUSTOM_DEFAULT_ERROR_MSG = "권한이 없습니다.";

    private static final String DEFAULT_ERROR_MSG = "접근이 거부되었습니다.";

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException {
        log.error(DEFAULT_ERROR_MSG);

        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");

        String errorMsg = accessDeniedException.getMessage().equals(DEFAULT_ERROR_MSG)
                ? CUSTOM_DEFAULT_ERROR_MSG
                : accessDeniedException.getMessage();

        ErrorResponseDto errorResponse = new ErrorResponseDto(errorMsg, HttpStatus.FORBIDDEN.value());

        String result = om.writeValueAsString(errorResponse);

        response.getWriter().write(result);

        response.setStatus(HttpStatus.FORBIDDEN.value());
    }
}


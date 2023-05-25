package com.week7.bannybannycarrotcarrot.security.exceptionhandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;

import com.week7.bannybannycarrotcarrot.dto.ErrorResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RequiredArgsConstructor
@Slf4j
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper om;

    private static final String DEFAULT_ERROR_MSG = "Full authentication is required to access this resource";
    private static final String CUSTOM_DEFAULT_ERROR_MSG = "문제가 발생했습니다.";


    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        log.error(DEFAULT_ERROR_MSG);

        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");

        String errorMsg = authException.getMessage();

        if(errorMsg.equals(DEFAULT_ERROR_MSG)){
            errorMsg = CUSTOM_DEFAULT_ERROR_MSG;
        }

        ErrorResponseDto errorResponse = new ErrorResponseDto(errorMsg, HttpStatus.UNAUTHORIZED.value());

        String result = om.writeValueAsString(errorResponse);

        response.getWriter().write(result);

        response.setStatus(HttpStatus.UNAUTHORIZED.value());
    }
}


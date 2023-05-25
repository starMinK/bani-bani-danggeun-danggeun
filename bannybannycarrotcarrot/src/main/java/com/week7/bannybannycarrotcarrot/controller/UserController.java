package com.week7.bannybannycarrotcarrot.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.week7.bannybannycarrotcarrot.dto.MsgDto;
import com.week7.bannybannycarrotcarrot.dto.UserDto;
import com.week7.bannybannycarrotcarrot.entity.User;
import com.week7.bannybannycarrotcarrot.security.jwt.JwtUtil;
import com.week7.bannybannycarrotcarrot.service.KakaoService;
import com.week7.bannybannycarrotcarrot.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final KakaoService kakaoService;

    @PostMapping("/signup")
    public MsgDto.ResponseDto signup(@RequestBody @Valid UserDto.SignupRequestDto requestDto) {
        return userService.signup(requestDto);
    }

    @PostMapping("/login")
    public MsgDto.DataResponseDto login(@RequestBody UserDto.LoginRequestDto requestDto, HttpServletResponse httpServletResponse) {
        return userService.login(requestDto, httpServletResponse);
    }

    @GetMapping("idcheck/{username}")
    public MsgDto.ResponseDto idCheck(@PathVariable String username) {

        return userService.idCheck(username);
    }

    @GetMapping("nicknamecheck/{nickname}")
    public MsgDto.ResponseDto nicknameCheck(@PathVariable String nickname) {
        return userService.nicknameCheck(nickname);
    }

    // kakao OAuth 를 위해 추가 -종열
    @GetMapping("/kakao/callback")
    public MsgDto.DataResponseDto kakaoLogin(@RequestParam String code, HttpServletResponse response) throws JsonProcessingException {
        // code: 카카오 서버로부터 받은 인가 코드
//        String createToken = kakaoService.kakaoLogin(code, response);

//         Cookie 생성 및 직접 브라우저에 Set
//        Cookie cookie = new Cookie(JwtUtil.AUTHORIZATION_HEADER, createToken.substring(7));
//        cookie.setPath("/");
//        response.addCookie(cookie);

//        return "redirect:/api/main";
        return kakaoService.kakaoLogin(code, response);

    }

}

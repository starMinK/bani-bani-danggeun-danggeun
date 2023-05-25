package com.week7.bannybannycarrotcarrot.service;

import com.week7.bannybannycarrotcarrot.dto.MsgDto;
import com.week7.bannybannycarrotcarrot.dto.UserDto;
import com.week7.bannybannycarrotcarrot.entity.User;
import com.week7.bannybannycarrotcarrot.errorcode.CommonStatusCode;
import com.week7.bannybannycarrotcarrot.errorcode.UserStatusCode;
import com.week7.bannybannycarrotcarrot.exception.RestApiException;
import com.week7.bannybannycarrotcarrot.repository.UserRepository;
import com.week7.bannybannycarrotcarrot.security.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.util.Objects;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class UserService {

    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    private final PasswordEncoder passwordEncoder;

    public MsgDto.ResponseDto signup(UserDto.SignupRequestDto requestDto) {
        String password = "";

        if(userRepository.existsByUsername(requestDto.username()))
            throw new RestApiException(UserStatusCode.OVERLAPPED_USERNAME);

        if(userRepository.existsByNickname(requestDto.nickname()))
            return new MsgDto.ResponseDto("이미 존재하는 닉네임입니다.", HttpStatus.BAD_REQUEST.value());


        if (!Objects.equals(requestDto.passwordCheck(), requestDto.password()))
            throw new RestApiException(UserStatusCode.PASSWORD_CHECK);

        password = passwordEncoder.encode(requestDto.password());
        userRepository.save(new User(requestDto.username(), password, requestDto.nickname()));
        return new MsgDto.ResponseDto(UserStatusCode.USER_SIGNUP_SUCCESS);
    }

    public MsgDto.DataResponseDto login(UserDto.LoginRequestDto dto, HttpServletResponse httpServletResponse){
        //아직 인증 전 객체
        UsernamePasswordAuthenticationToken beforeAuthentication = new UsernamePasswordAuthenticationToken(dto.username(), dto.password());

        //인증 완료된 인증 객체
        Authentication afterAuthentication = authenticationManagerBuilder.getObject().authenticate(beforeAuthentication);

        //인증 완료된 객체로 JWT 생성
        httpServletResponse.addHeader(JwtUtil.AUTHORIZATION_HEADER, jwtUtil.generateToken(afterAuthentication));

        User user = userRepository.findByUsername(dto.username()).orElseThrow(
                () -> new RestApiException(CommonStatusCode.NOT_FIND_USER)
        );

        return new MsgDto.DataResponseDto(UserStatusCode.USER_LOGIN_SUCCESS,user.getNickname());
    }

    public MsgDto.ResponseDto idCheck(String username) {
        if (userRepository.existsByUsername(username)) {
            throw  new RestApiException(UserStatusCode.OVERLAPPED_USERNAME);
        }
        //아이디유효성검사
        if (Pattern.matches("^.(?=.*\\d)(?=.*[a-z]).*$",username)){
            if(username.length() < 5 || username.length() > 10){
                throw new RestApiException(UserStatusCode.WRONG_USERNAME_SIZE);
            }

        } else {
            throw new RestApiException(UserStatusCode.WRONG_USERNAME_PATTERN2);
        }
        return new MsgDto.ResponseDto(UserStatusCode.AVAILABLE_USERNAME);
    }

    public MsgDto.ResponseDto nicknameCheck(String nickname) {
        if (userRepository.existsByNickname(nickname)){
            throw new RestApiException(UserStatusCode.OVERLAPPED_NICKNAME);
        }
        return new MsgDto.ResponseDto(UserStatusCode.AVAILABLE_NICKNAME);
    }
}

package com.week7.bannybannycarrotcarrot.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.week7.bannybannycarrotcarrot.dto.KakaoUserInfoDto;
import com.week7.bannybannycarrotcarrot.dto.MsgDto;
import com.week7.bannybannycarrotcarrot.dto.TokenDto;
import com.week7.bannybannycarrotcarrot.entity.RefreshToken;
import com.week7.bannybannycarrotcarrot.entity.User;
import com.week7.bannybannycarrotcarrot.entity.UserRole;
import com.week7.bannybannycarrotcarrot.errorcode.UserStatusCode;
import com.week7.bannybannycarrotcarrot.interfacepackage.Logininterface;
import com.week7.bannybannycarrotcarrot.repository.RefreshTokenRepository;
import com.week7.bannybannycarrotcarrot.repository.SocialAccessTokenRepository;
import com.week7.bannybannycarrotcarrot.repository.UserRepository;
import com.week7.bannybannycarrotcarrot.security.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletResponse;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class KakaoService implements Logininterface {
    public final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    public   final UserRepository userRepository;
    public final JwtUtil jwtUtil;

    //  https://kauth.kakao.com/oauth/authorize?client_id={REST_API_KEY}&redirect_uri={REDIRECT_URI}&response_type=code

    //  https://kauth.kakao.com/oauth/authorize?client_id=d6c5b10cf544ae8fcc0cbb0bbc530328&redirect_uri=http://banibanipj.s3-website.ap-northeast-2.amazonaws.com/api/user/kakao/callback&response_type=code



    public MsgDto.DataResponseDto kakaoLogin(String code, HttpServletResponse response) throws JsonProcessingException {
        // 1. "인가 코드"로 "액세스 토큰" 요청
        String accessToken = getToken(code);

        // 2. 토큰으로 카카오 API 호출 : "액세스 토큰"으로 "카카오 사용자 정보" 가져오기
        KakaoUserInfoDto kakaoUserInfo = getKakaoUserInfo(accessToken);

        // 3. 필요시에 회원가입
        User kakaoUser = registerKakaoUserIfNeeded(kakaoUserInfo);

//        forceLoginUser(kakaoUser);
//        createToken(kakaoUser, response);

        String Token = jwtUtil.createToken(kakaoUser.getId());
        response.addHeader(JwtUtil.AUTHORIZATION_HEADER, Token);



//        UsernamePasswordAuthenticationToken beforeAuthentication = new UsernamePasswordAuthenticationToken(kakaoUserInfo.getEmail(),kakaoUser.getUserRole());
//
//        //인증 완료된 인증 객체
////        Authentication afterAuthentication = authenticationManagerBuilder.getObject().authenticate(beforeAuthentication);
//        String generateToken = jwtUtil.generateToken(beforeAuthentication);
//        //인증 완료된 객체로 JWT 생성
//        response.addHeader(JwtUtil.AUTHORIZATION_HEADER, generateToken);



//         4. JWT 토큰 반환
//        String createToken =  jwtUtil.generateToken(kakaoUser.getUsername());
//        response.addHeader(JwtUtil.AUTHORIZATION_HEADER, generateToken);
        return new MsgDto.DataResponseDto(UserStatusCode.USER_LOGIN_SUCCESS, kakaoUser.getNickname());
    }

    // 1. "인가 코드"로 "액세스 토큰" 요청
    private String getToken(String code) throws JsonProcessingException {
        // HTTP Header 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // HTTP Body 생성
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", "d6c5b10cf544ae8fcc0cbb0bbc530328");
        body.add("redirect_uri", "http://banibanipj.s3-website.ap-northeast-2.amazonaws.com/api/user/kakao/callback");
        body.add("code", code);

        // HTTP 요청 보내기
        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest =
                new HttpEntity<>(body, headers);
        RestTemplate rt = new RestTemplate();
        ResponseEntity<String> response = rt.exchange(
                "https://kauth.kakao.com/oauth/token",
                HttpMethod.POST,
                kakaoTokenRequest,
                String.class
        );

        // HTTP 응답 (JSON) -> 액세스 토큰 파싱
        String responseBody = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);
        return jsonNode.get("access_token").asText();
    }

    // 2. 토큰으로 카카오 API 호출 : "액세스 토큰"으로 "카카오 사용자 정보" 가져오기
    private KakaoUserInfoDto getKakaoUserInfo(String accessToken) throws JsonProcessingException {
        // HTTP Header 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // HTTP 요청 보내기
        HttpEntity<MultiValueMap<String, String>> kakaoUserInfoRequest = new HttpEntity<>(headers);
        RestTemplate rt = new RestTemplate();
        ResponseEntity<String> response = rt.exchange(
                "https://kapi.kakao.com/v2/user/me",
                HttpMethod.POST,
                kakaoUserInfoRequest,
                String.class
        );

        String responseBody = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);
        Long id = jsonNode.get("id").asLong();
        String nickname = jsonNode.get("properties")
                .get("nickname").asText();
        String email = jsonNode.get("kakao_account")
                .get("email").asText();

        log.info("카카오 사용자 정보: " + id + ", " + nickname + ", " + email);
        return new KakaoUserInfoDto(id, nickname, email);
    }

    // 3. 필요시에 회원가입
    private User registerKakaoUserIfNeeded(KakaoUserInfoDto kakaoUserInfo) {
        // DB 에 중복된 Kakao Id 가 있는지 확인
        Long kakaoId = kakaoUserInfo.getId();
        User kakaoUser = userRepository.findByKakaoId(kakaoId)
                .orElse(new User());
        if (kakaoUser.getKakaoId() == null) {
                // 신규 회원가입
                // password: random UUID
                String password = UUID.randomUUID().toString();
                String encodedPassword = passwordEncoder.encode(password);

                // email: kakao email
                String email = kakaoUserInfo.getEmail();
                kakaoUser = new User(kakaoUserInfo.getNicknmae(), kakaoId, encodedPassword, email, UserRole.USER);
                userRepository.save(kakaoUser);
        }
        return kakaoUser;
    }

//    public void createToken(User user, HttpServletResponse response) {
//        TokenDto tokenDto = jwtUtil.createAllToken(user.getEmail());
//
//        Optional<RefreshToken> refreshToken = refreshTokenRepository.findByAccountEmail(user.getEmail());
//
//        if (refreshToken.isPresent()) {
//            refreshTokenRepository.save(refreshToken.get().updateToken(tokenDto.getRefreshToken()));
//        } else {
//            RefreshToken newToken = new RefreshToken(tokenDto.getRefreshToken(), user.getEmail());
//            refreshTokenRepository.save(newToken);
//        }
//
//        setHeader(response, tokenDto);
//    }





}

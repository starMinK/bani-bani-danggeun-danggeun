package com.week7.bannybannycarrotcarrot.interfacepackage;

import com.week7.bannybannycarrotcarrot.dto.TokenDto;
import com.week7.bannybannycarrotcarrot.entity.User;
import com.week7.bannybannycarrotcarrot.repository.UserRepository;
import com.week7.bannybannycarrotcarrot.security.UserDetailsImpl;
import com.week7.bannybannycarrotcarrot.security.jwt.JwtUtil;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import javax.servlet.http.HttpServletResponse;

public interface Logininterface {
    default void setHeader(HttpServletResponse response, TokenDto tokenDto) {
        response.addHeader(JwtUtil.ACCESS_TOKEN, tokenDto.getAccessToken());
        response.addHeader(JwtUtil.REFRESH_TOKEN, tokenDto.getRefreshToken());
    }

    default void forceLoginUser(User user) {
        UserDetails userDetails = new UserDetailsImpl(user);


        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

}

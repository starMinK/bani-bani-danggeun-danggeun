package com.week7.bannybannycarrotcarrot.security;

import com.week7.bannybannycarrotcarrot.entity.User;
import com.week7.bannybannycarrotcarrot.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Collections;

@RequiredArgsConstructor
@Component
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        return userRepository.findByUsername(username)
                .map(this::createUserDetails)
                .orElse(null);
    }

    private UserDetails createUserDetails(User user){

        GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(user.getUserRole().toString());

        //userdetails 내부의 username에 userId를 넣어둡니다(DB에 저장된 ID값)
        return new org.springframework.security.core.userdetails.User(
                String.valueOf(user.getId()),
                user.getPassword(),
                Collections.singleton(grantedAuthority));
    }
}
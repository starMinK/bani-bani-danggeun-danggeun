package com.week7.bannybannycarrotcarrot.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class User extends TimeStamped{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column
    private UserRole userRole;


    private Long kakaoId;  // kakao OAuth 를 위해 추가 -종열

    private String email;

    @Column(nullable = false)
    private  String nickname;


    @Builder
    public User(String username, String password, String nickname) {
        this.username = username;
        this.password = password;
        this.userRole = UserRole.USER;
        this.nickname = nickname;
    }


    // kakao OAuth 를 위해 추가 -종열
    public User(String username, Long kakaoId, String password, String email, UserRole userRole) {
        this.username = username;
        this.kakaoId = kakaoId;
        this.password = password;
        this.email = email;
        this.nickname = username;
        this.userRole = UserRole.USER;
    }

    public User kakaoIdUpdate(Long kakaoId) {
        this.kakaoId = kakaoId;
        return this;
    }

}

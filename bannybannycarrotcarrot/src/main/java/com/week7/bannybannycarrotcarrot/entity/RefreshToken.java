package com.week7.bannybannycarrotcarrot.entity;


import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;

@Data
@Getter
@Entity
@NoArgsConstructor
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long refreshId;

    @NotBlank
    private String refreshToken;

    private String accountEmail;

    public RefreshToken updateToken(String token) {
        this.refreshToken = token;
        return this;
    }

    @Builder
    public RefreshToken(String refreshToken, String accountEmail) {
        this.refreshToken = refreshToken;
        this.accountEmail = accountEmail;
    }


}

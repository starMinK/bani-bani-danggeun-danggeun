package com.week7.bannybannycarrotcarrot.dto;

import com.week7.bannybannycarrotcarrot.entity.User;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class UserDto {


    public record SignupRequestDto(@NotBlank(message = "아이디가 입력되지 않았습니다.")
                                   @Size(min = 5, max = 10, message = "아이디는 5자 이상 10자 이하만 가능합니다.")
                                   @Pattern (regexp="^(?=.*?[0-9])(?=.*?[a-z]).{5,10}$", message = "아이디는 소문자 영문, 숫자로를 필수로 포함하여야 합니다.")
                                   String username,

                                   @Size(min = 8, max = 15)
                                   @Pattern (regexp="^.(?=.*\\d)(?=.*[a-zA-Z])(?=.*[!@#$%^&+=]).*$", message = "비밀번호는 대소문자 영문, 특수문자를 필수로 포함하여야 합니다.")
                                   @NotBlank(message = "비밀번호가 입력되지 않았습니다.")
                                   String password,

                                   @NotBlank(message = "비밀번호 확인(이)가 입력되지 않았습니다.")
                                   String passwordCheck,

                                   @NotBlank(message = "닉네임이 입력되지 않았습니다.")
                                   String nickname) {

        public User toEntity() {
            return new User(this.username, this.password, this.nickname);
        }
    }

    public record LoginRequestDto(@NotBlank(message = "아이디가 입력되지 않았습니다.")
                                  String username,

                                  @NotBlank(message = "비밀번호가 입력되지 않았습니다.")
                                  String password) {
    }
}

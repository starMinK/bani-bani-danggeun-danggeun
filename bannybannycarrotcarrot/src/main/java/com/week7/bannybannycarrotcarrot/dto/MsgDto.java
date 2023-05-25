package com.week7.bannybannycarrotcarrot.dto;


import com.week7.bannybannycarrotcarrot.errorcode.StatusCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Setter
@Getter
@NoArgsConstructor
public class MsgDto {
    public record ResponseDto(String msg, int statusCode) {
        public ResponseDto(StatusCode statusCode) {

            this(statusCode.getMsg(), statusCode.getStatusCode());
        }
    }

    public record DataResponseDto(String msg, int statusCode, Object data) {
        public DataResponseDto(StatusCode statusCode, Object data) {
            this(statusCode.getMsg(), statusCode.getStatusCode(), data);
        }
    }
}
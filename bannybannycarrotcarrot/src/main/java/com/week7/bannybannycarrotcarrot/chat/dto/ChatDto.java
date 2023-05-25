package com.week7.bannybannycarrotcarrot.chat.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ChatDto {

    private String message;
    private String sender;

    private LocalDateTime createdAt;

    public ChatDto(String message, String sender, LocalDateTime createAt) {
        this.message = message;
        this.sender = sender;
        this.createdAt = createAt;
    }
}

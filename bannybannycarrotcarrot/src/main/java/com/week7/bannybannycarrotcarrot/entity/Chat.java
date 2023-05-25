package com.week7.bannybannycarrotcarrot.entity;

import com.week7.bannybannycarrotcarrot.chat.dto.ChatMessage;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@Getter
@EntityListeners(AuditingEntityListener.class)
public class Chat{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private ChatMessage.MessageType type; // 메시지 타입

    @Column(nullable = false)
    private Long roomId; // 방번호

    @Column(nullable = false)
    private String sender; // 메시지 보낸사람

    @Column
    private String message; // 메시지

    @Column
    private LocalDateTime createAt;

    public Chat(ChatMessage message, LocalDateTime createAt) {
        this.type = message.getType();
        this.roomId = message.getRoomId();
        this.sender = message.getSender();
        this.message = message.getMessage();
        this.createAt = createAt;
    }
}

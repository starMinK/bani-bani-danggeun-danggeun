package com.week7.bannybannycarrotcarrot.chat.controller;

import com.week7.bannybannycarrotcarrot.chat.dto.ChatDto;
import com.week7.bannybannycarrotcarrot.chat.dto.ChatMessage;
import com.week7.bannybannycarrotcarrot.chat.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class ChatController {

    private final SimpMessageSendingOperations messagingTemplate;
    private final ChatService chatService;

    @MessageMapping("/chat/message")
    public void message(ChatMessage message) {
        chatService.save(message);
        if (ChatMessage.MessageType.ENTER.equals(message.getType())) {
            message.setMessage(message.getSender() + "님이 입장하셨습니다.");
        }
        messagingTemplate.convertAndSend("/sub/chat/room/" + message.getRoomId(), message);
    }

    @GetMapping("/chat/getmessage/{roomId}")
    public List<ChatDto> getMessage(@PathVariable Long roomId) {
        return chatService.getMessage(roomId);
    }
}
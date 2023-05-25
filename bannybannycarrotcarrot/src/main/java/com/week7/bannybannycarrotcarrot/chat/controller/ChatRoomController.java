package com.week7.bannybannycarrotcarrot.chat.controller;

import com.week7.bannybannycarrotcarrot.chat.dto.ChatRoom;
import com.week7.bannybannycarrotcarrot.chat.service.ChatRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/chat")
public class ChatRoomController {

    private final ChatRoomService chatRoomService;

    // 채팅방 생성
    @PostMapping("/room")
    public ChatRoom createRoom(@RequestParam Long postId, @AuthenticationPrincipal UserDetails userDetails) {
        return chatRoomService.createChatRoom(postId, Long.parseLong(userDetails.getUsername()));
    }

    @GetMapping("/rooms/{loginNickname}")
    public List<ChatRoom> getRoomList(@PathVariable String loginNickname) {
        return chatRoomService.getChatRoom(loginNickname);
    }
}

package com.week7.bannybannycarrotcarrot.chat.dto;

import com.week7.bannybannycarrotcarrot.entity.Post;
import lombok.Builder;
import lombok.Getter;

@Getter
//@Builder
public class ChatRoom {
    private Long roomId;
    private String postUserNickname;

    private String title;

    private String content;

    private String image;

    private int price;

    private String location;

    public ChatRoom(Long roomId, Post post) {
        this.roomId = roomId;
        this.postUserNickname = post.getNickname();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.image = post.getImage();
        this.price = post.getPrice();
        this.location = post.getLocation();
    }
}

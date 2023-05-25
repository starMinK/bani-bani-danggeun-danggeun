package com.week7.bannybannycarrotcarrot.dto;

import com.week7.bannybannycarrotcarrot.entity.Post;
import lombok.Setter;

@Setter
public class PostDto {

    public record PostRequestDto(String title,
                                 String content,
                                 String image,
                                 int price,
                                 String location
                                 ){

    }
    public record PostResponseDto(
            Long id,
            String title,
            String content,
            String image,
            int price,
            String location,
            String nickname) {
        public PostResponseDto(Post post) {
                this(post.getId(), post.getTitle(), post.getContent(), post.getImage(),
                    post.getPrice(), post.getLocation(),post.getNickname());
        }

    }



}

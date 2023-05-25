package com.week7.bannybannycarrotcarrot.controller;

import com.week7.bannybannycarrotcarrot.dto.MsgDto;
import com.week7.bannybannycarrotcarrot.dto.PostDto;
import com.week7.bannybannycarrotcarrot.security.UserDetailsImpl;
import com.week7.bannybannycarrotcarrot.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.function.EntityResponse;

import java.util.List;

@RestController
@RequestMapping("/api/post")
@RequiredArgsConstructor
@Slf4j
public class PostController {

    private final PostService postService;
    //작성
    @PostMapping("")
    public ResponseEntity<?> post(@RequestBody PostDto.PostRequestDto requestDto, @AuthenticationPrincipal UserDetails userDetails){
        return postService.post(requestDto, Long.parseLong(userDetails.getUsername()));
    }
    //전체조회
    @GetMapping("")
    public List<PostDto.PostResponseDto> getPosts(){
        return postService.getPosts();
    }

    @GetMapping("/{postId}")
    public PostDto.PostResponseDto getPost(@PathVariable Long postId){
        return postService.getPost(postId);
    }

    @PatchMapping("/{postId}")
    public MsgDto.DataResponseDto updatePost(@PathVariable Long postId, @RequestBody PostDto.PostRequestDto requestDto, @AuthenticationPrincipal UserDetails userDetails){
        return postService.updatePost(postId, requestDto, Long.parseLong(userDetails.getUsername()));
    }

    @DeleteMapping("/{postId}")
    public MsgDto.ResponseDto deletePost(@PathVariable Long postId, @AuthenticationPrincipal UserDetails userDetails){
        return postService.deletePost(postId, Long.parseLong(userDetails.getUsername()));
    }

}

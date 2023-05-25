package com.week7.bannybannycarrotcarrot.service;

import com.week7.bannybannycarrotcarrot.dto.MsgDto;
import com.week7.bannybannycarrotcarrot.dto.PostDto;
import com.week7.bannybannycarrotcarrot.entity.Post;
import com.week7.bannybannycarrotcarrot.entity.User;
import com.week7.bannybannycarrotcarrot.errorcode.CommonStatusCode;
import com.week7.bannybannycarrotcarrot.errorcode.PostStatusCode;
import com.week7.bannybannycarrotcarrot.exception.RestApiException;
import com.week7.bannybannycarrotcarrot.repository.PostRepository;
import com.week7.bannybannycarrotcarrot.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    private final UserRepository userRepository;

    @Transactional
    public ResponseEntity<?> post(PostDto.PostRequestDto requestDto, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new RestApiException(CommonStatusCode.NOT_FIND_USER)
        );

        Post post = new Post(requestDto, user.getNickname());
        System.out.println("-------------------------------------");
        postRepository.save(post);
        return ResponseEntity.ok(new MsgDto.DataResponseDto(PostStatusCode.CREATE_POST, new PostDto.PostResponseDto(post)));
    }


    @Transactional(readOnly = true)
    public List<PostDto.PostResponseDto> getPosts() {
        List<Post> posts = postRepository.findAllByOrderByIdDesc();
        return posts.stream().map(p -> new PostDto.PostResponseDto(p)).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public PostDto.PostResponseDto getPost(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new RestApiException(CommonStatusCode.NOT_FIND_POST)
        );
        return new PostDto.PostResponseDto(post);
    }

    @Transactional
    public MsgDto.DataResponseDto updatePost(Long postId, PostDto.PostRequestDto requestDto, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new RestApiException(CommonStatusCode.NOT_FIND_USER)
        );
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new RestApiException(CommonStatusCode.NOT_FIND_POST)
        );
        if(!post.getNickname().equals(user.getNickname())){
            throw new RestApiException(PostStatusCode.INVALID_USER_UPDATE);
        }
        post.update(requestDto);
        return new MsgDto.DataResponseDto(PostStatusCode.UPDATE_POST ,new PostDto.PostResponseDto(post));
    }

    @Transactional
    public MsgDto.ResponseDto deletePost(Long postId, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new RestApiException(CommonStatusCode.NOT_FIND_USER)
        );
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new RestApiException(CommonStatusCode.NOT_FIND_POST)
        );
        if(!post.getNickname().equals(user.getNickname())){
            throw new RestApiException(PostStatusCode.INVALID_USER_DELETE);
        }
        postRepository.deleteById(postId);
        return new MsgDto.ResponseDto(PostStatusCode.DELETE_POST);
    }
}

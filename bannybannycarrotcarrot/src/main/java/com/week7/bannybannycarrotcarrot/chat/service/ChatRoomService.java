package com.week7.bannybannycarrotcarrot.chat.service;

import com.week7.bannybannycarrotcarrot.chat.dto.ChatRoom;
import com.week7.bannybannycarrotcarrot.entity.Post;
import com.week7.bannybannycarrotcarrot.entity.Room;
import com.week7.bannybannycarrotcarrot.entity.RoomDetail;
import com.week7.bannybannycarrotcarrot.entity.User;
import com.week7.bannybannycarrotcarrot.errorcode.ChatStatusCode;
import com.week7.bannybannycarrotcarrot.exception.RestApiException;
import com.week7.bannybannycarrotcarrot.repository.PostRepository;
import com.week7.bannybannycarrotcarrot.repository.RoomDetailRepository;
import com.week7.bannybannycarrotcarrot.repository.RoomRepository;
import com.week7.bannybannycarrotcarrot.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatRoomService {

    private final PostRepository postRepository;
    private final RoomRepository roomRepository;

    private final UserRepository userRepository;
    private final RoomDetailRepository roomDetailRepository;

    @Transactional
    public ChatRoom createChatRoom(Long postId, Long userId) {
        Post post = postRepository.findById(postId).orElseThrow(EntityNotFoundException::new);
        String userNickname = userRepository.getReferenceById(userId).getNickname();

        RoomDetail roomDetail = roomDetailRepository.findByPostNicknameAndLoginNickname(post.getNickname(), userNickname)
                .orElse(null);

        //존재 한다면.
        if (roomDetail!=null) return new ChatRoom(roomDetail.getRoom().getId(), post);

        //존재하지 않는다면.
        Room room = new Room(post);
        roomRepository.save(room);

        RoomDetail createdRoomDetail = new RoomDetail(post.getNickname(), userNickname, room);
        roomDetailRepository.save(createdRoomDetail);

        return new ChatRoom(room.getId(), post);
    }

    public List<ChatRoom> getChatRoom(String loginNickname) {
        List<ChatRoom> chatRoomList = new ArrayList<>();
        List<RoomDetail> roomDetails = roomDetailRepository.findAllByLoginNickname(loginNickname);

        if(roomDetails.isEmpty())
            throw new RestApiException(ChatStatusCode.NO_ARRAY_EXCEPTION);

        for (RoomDetail roomDetail : roomDetails) {
            Room room = roomRepository.findById(roomDetail.getRoom().getId()).orElse(null);

            Post post = room.getPost();
            ChatRoom chatRoom = new ChatRoom(room.getId(), post);
            chatRoomList.add(chatRoom);
        }
        return chatRoomList;
    }
}

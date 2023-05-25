package com.week7.bannybannycarrotcarrot.repository;

import com.week7.bannybannycarrotcarrot.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoomRepository extends JpaRepository<Room,Long> {
    List<Room> findByPostId(Long postId);
}

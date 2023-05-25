package com.week7.bannybannycarrotcarrot.repository;

import com.week7.bannybannycarrotcarrot.entity.RoomDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RoomDetailRepository extends JpaRepository<RoomDetail, Long> {

    Optional<RoomDetail> findByPostNicknameAndLoginNickname(String postNickname, String loginNickname);

    List<RoomDetail> findAllByLoginNickname(String loginNickname);
}
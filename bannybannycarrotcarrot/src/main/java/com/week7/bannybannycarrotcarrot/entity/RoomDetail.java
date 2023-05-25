package com.week7.bannybannycarrotcarrot.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Getter
public class RoomDetail extends TimeStamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Room room;

    @Column(nullable = false)
    private String postNickname;

    @Column(nullable = false)
    private String loginNickname;

    public RoomDetail(String postNickname, String loginNickname, Room room) {
        this.postNickname = postNickname;
        this.loginNickname = loginNickname;
        this.room = room;
        this.room.getRoomDetails().add(this);
    }
}

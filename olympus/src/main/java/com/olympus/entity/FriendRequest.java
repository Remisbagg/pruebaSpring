package com.olympus.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "friend_request")
@NoArgsConstructor
public class FriendRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "request_id")
    private Long id;

    @ManyToOne(targetEntity = User.class)
    @JoinColumn(name = "sender_id", nullable = false)
    private User sender;

    @ManyToOne(targetEntity = User.class)
    @JoinColumn(name = "receiver_id", nullable = false)
    private User receiver;

    public FriendRequest(Long senderId, Long receiverId) {
        User sender = new User(senderId);
        User receiver = new User(receiverId);
        this.sender = sender;
        this.receiver = receiver;
    }
}

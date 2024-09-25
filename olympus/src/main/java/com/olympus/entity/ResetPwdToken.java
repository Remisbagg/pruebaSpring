package com.olympus.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@Table(name = "reset_password")
public class ResetPwdToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reset_id")
    private Long id;

    @OneToOne(targetEntity = User.class)
    @JoinColumn(name = "user_id", unique = true, nullable = false)
    private User user;

    @Column(name = "token", unique = true, nullable = false)
    private String token;

    @Column(name = "created_time", nullable = false)
    private LocalDateTime createdTime;

    public ResetPwdToken(User user, String token) {
        this.user = user;
        this.token = token;
        this.createdTime = LocalDateTime.now();
    }
}

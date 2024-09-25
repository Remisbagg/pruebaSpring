package com.olympus.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "authentication")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Authentication {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "code_id")
    private Long id;

    @OneToOne(targetEntity = User.class)
    @JoinColumn(name = "user_id", unique = true, nullable = false)
    private User user;

    @Column(name = "code", nullable = false)
    private String code;

    @Column(name = "created_time", nullable = false)
    private LocalDateTime createdTime;

    public Authentication(User user, String code) {
        this.user = user;
        this.code = code;
        this.createdTime = LocalDateTime.now();
    }
}

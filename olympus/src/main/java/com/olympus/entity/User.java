package com.olympus.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "avatar")
    private String avatar;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "birthdate")
    private LocalDate birthDate;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "current_address")
    private String currentAddress;

    @Column(name = "occupation")
    private String occupation;

    @Column(name = "delete_status")
    private boolean deleteStatus;

    @Column(name = "gender")
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private MaritalStatus status;

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private Role role;

    public User() {
        this.gender = Gender.RATHER_NOT_TO_SAY;
        this.status = MaritalStatus.RATHER_NOT_TO_SAY;
        this.role = Role.ROLE_USER;
    }

    public User(String id) {
        this.id = Long.valueOf(id);
    }

    public User(Long id) {
        this.id = id;
    }
}

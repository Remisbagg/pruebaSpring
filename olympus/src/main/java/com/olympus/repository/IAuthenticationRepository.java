package com.olympus.repository;

import com.olympus.entity.Authentication;
import com.olympus.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IAuthenticationRepository extends JpaRepository<Authentication, Long> {
    Optional<Authentication> findAuthenticationByUser_Email(String email);

    boolean existsByUserIdAndCode(Long userId, String code);

    void deleteByUser(User user);

    Optional<Authentication> findByUser(User user);
}

package com.olympus.repository;

import com.olympus.entity.ResetPwdToken;
import com.olympus.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IResetPwdTokenRepository extends JpaRepository<ResetPwdToken, Long> {
    Optional<ResetPwdToken> findByTokenAndUser_Email(String token, String email);

    Optional<ResetPwdToken> findByUser(User user);

    Optional<ResetPwdToken> findByToken(String hashedToken);
}

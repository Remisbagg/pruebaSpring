package com.back.service;

import com.back.dto.request.AccountLogin;
import com.back.dto.request.AccountPasswordReset;
import com.back.dto.request.AccountRegister;
import com.back.dto.request.UserUpdate;
import com.back.dto.response.CurrentUserProfile;
import com.back.dto.response.OtherUserProfile;
import com.back.entity.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface IUserService {

    Optional<User> findUserByEmail(String email);

    Long register(AccountRegister accountRegister);

    boolean existsEmail(String email);

    boolean existsUserByEmailAndPassword(AccountLogin accountLogin);

    void updatePassword(AccountPasswordReset request);

    Long updateUser(Long userId, UserUpdate updateUser, MultipartFile file) throws IOException;

    boolean existByUserId(Long id);

    Long findIdByUserDetails(UserDetails userDetails);

    CurrentUserProfile getCurrentUserProfile(Long userId);

    OtherUserProfile getOtherUserProfile(Long currentUserId, Long targetUserId);

    List<OtherUserProfile> searchUsers(String keyword, Long userId);
}

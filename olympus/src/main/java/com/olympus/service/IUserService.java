package com.olympus.service;

import com.olympus.dto.request.AccountLogin;
import com.olympus.dto.request.AccountPasswordReset;
import com.olympus.dto.request.AccountRegister;
import com.olympus.dto.request.UserUpdate;
import com.olympus.dto.response.CurrentUserProfile;
import com.olympus.dto.response.OtherUserProfile;
import com.olympus.entity.User;
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

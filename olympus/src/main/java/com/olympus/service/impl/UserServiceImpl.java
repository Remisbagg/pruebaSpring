package com.olympus.service.impl;

import com.olympus.dto.request.AccountLogin;
import com.olympus.dto.request.AccountPasswordReset;
import com.olympus.dto.request.AccountRegister;
import com.olympus.dto.request.UserUpdate;
import com.olympus.dto.response.CurrentUserProfile;
import com.olympus.dto.response.OtherUserFriendRequest;
import com.olympus.dto.response.OtherUserFriendship;
import com.olympus.dto.response.OtherUserProfile;
import com.olympus.entity.FriendRequest;
import com.olympus.entity.Friendship;
import com.olympus.entity.User;
import com.olympus.exception.UserNotFoundException;
import com.olympus.mapper.CurrentUserProfileMapper;
import com.olympus.mapper.OtherUserProfileMapper;
import com.olympus.mapper.UserUpdateMapper;
import com.olympus.repository.IUserRepository;
import com.olympus.service.IFriendRequestService;
import com.olympus.service.IFriendshipService;
import com.olympus.service.IImageService;
import com.olympus.service.IUserService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Transactional
public class UserServiceImpl implements IUserService {
    private final IUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserUpdateMapper userUpdateMapper;
    private final CurrentUserProfileMapper currentUserProfileMapper;
    private final IImageService imageService;
    private final IFriendRequestService friendRequestService;
    private final IFriendshipService friendshipService;
    private final OtherUserProfileMapper otherUserProfileMapper;

    @Autowired
    UserServiceImpl(PasswordEncoder passwordEncoder, IUserRepository userRepository,
                    UserUpdateMapper userUpdateMapper, CurrentUserProfileMapper currentUserProfileMapper,
                    IImageService imageService, IFriendRequestService friendRequestService,
                    IFriendshipService friendshipService, OtherUserProfileMapper otherUserProfileMapper) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.userUpdateMapper = userUpdateMapper;
        this.currentUserProfileMapper = currentUserProfileMapper;
        this.imageService = imageService;
        this.friendRequestService = friendRequestService;
        this.friendshipService = friendshipService;
        this.otherUserProfileMapper = otherUserProfileMapper;
    }

    @Override
    public Optional<User> findUserByEmail(String email) {
        return userRepository.findUserByEmail(email);
    }

    @Override
    public Long findIdByUserDetails(UserDetails userDetails) {
        String email = userDetails.getUsername();
        Optional<User> user = findUserByEmail(email);
        return user.map(User::getId).orElse(null);
    }

    @Override
    public Long register(AccountRegister accountRegister) {
        User user = new User();
        user.setEmail(accountRegister.getEmail().trim().toLowerCase());
        user.setPassword(passwordEncoder.encode(accountRegister.getPassword()));
        return userRepository.save(user).getId();
    }

    @Override
    public boolean existsEmail(String email) {
        return userRepository.existsByEmail(email.trim().toLowerCase());
    }

    @Override
    public boolean existsUserByEmailAndPassword(AccountLogin accountLogin) {
        String email = accountLogin.getEmail().trim().toLowerCase();
        String rawPassword = accountLogin.getPassword();
        Optional<User> user = userRepository.findUserByEmail(email);
        if (user.isPresent()) {
            User existedUser = user.get();
            String storedPassword = existedUser.getPassword();
            return passwordEncoder.matches(rawPassword, storedPassword);
        }
        return false;
    }

    @Override
    public void updatePassword(AccountPasswordReset request) {
        String newPassword = request.getPassword();
        String email = request.getEmail().trim().toLowerCase();
        User user = userRepository.findUserByEmail(email).orElseThrow(() -> new UserNotFoundException(email + " not found"));
        if (user.isDeleteStatus()) {
            throw new UserNotFoundException(email + " not found");
        }
        String encryptedPwd = passwordEncoder.encode(newPassword);
        user.setPassword(encryptedPwd);
        userRepository.save(user);
    }

    @Override
    public boolean existByUserId(Long id) {
        return userRepository.existsById(id);
    }

    @Override
    public java.lang.Long updateUser(Long userId, UserUpdate updateUser, MultipartFile file) throws IOException {
        String imageUrl = "";
        if (file != null) {
            String fileName = imageService.save(file);
            imageUrl = imageService.getImageUrl(fileName);
        }

        User user = userRepository.findUserById(userId).orElseThrow();
        if (!imageUrl.isEmpty()) {
            user.setAvatar(imageUrl);
        }
        userUpdateMapper.updateEntityFromDTO(updateUser, user);
        return userRepository.save(user).getId();
    }

    @Override
    public CurrentUserProfile getCurrentUserProfile(Long userId) {
        User user = userRepository.getReferenceById(userId);
        return currentUserProfileMapper.toDTO(user);
    }

    @Override
    public OtherUserProfile getOtherUserProfile(Long currentUserId, Long targetUserId) {
        User targetUser = userRepository.getReferenceById(targetUserId);
        OtherUserFriendship friendship = new OtherUserFriendship();
        OtherUserFriendRequest friendRequest = new OtherUserFriendRequest();
        if (friendshipService.existsFriendship(currentUserId, targetUserId)) {
            Friendship existedFriendship = friendshipService.findByUserIds(currentUserId, targetUserId);
            friendship.setFriendshipId(existedFriendship.getId());
            friendship.setStatus(true);
        }
        FriendRequest existedFriendRequest = friendRequestService.findByUserIds(currentUserId, targetUserId);
        if (existedFriendRequest != null) {
            friendRequest.setRequestId(existedFriendRequest.getId());
            friendRequest.setStatus(true);
            friendRequest.setRole(friendRequestService.identifyRole(currentUserId, targetUserId));
        }
        OtherUserProfile profile = otherUserProfileMapper.toDTO(targetUser);
        profile.setFriendship(friendship);
        profile.setFriendRequest(friendRequest);
        return profile;
    }

    @Override
    public List<OtherUserProfile> searchUsers(String keyword, Long userId) {
        List<User> users = userRepository.findByFirstNameContainingOrLastNameContainingOrEmailContaining(keyword, keyword, keyword);
        List<User> filteredUsers = users.stream().filter(u -> !Objects.equals(u.getId(), userId)).toList();
        List<OtherUserProfile> searchResult = new ArrayList<>();
        for (User u : filteredUsers) {
            Long targetUserId = u.getId();
            OtherUserFriendship friendship = new OtherUserFriendship();
            OtherUserFriendRequest friendRequest = new OtherUserFriendRequest();
            if (friendshipService.existsFriendship(userId, u.getId())) {
                Friendship existedFriendship = friendshipService.findByUserIds(userId, targetUserId);
                friendship.setFriendshipId(existedFriendship.getId());
                friendship.setStatus(true);
            }
            FriendRequest existedFriendRequest = friendRequestService.findByUserIds(userId, targetUserId);
            if (existedFriendRequest != null) {
                friendRequest.setRequestId(existedFriendRequest.getId());
                friendRequest.setStatus(true);
                friendRequest.setRole(friendRequestService.identifyRole(userId, targetUserId));
            }
            OtherUserProfile profile = otherUserProfileMapper.toDTO(u);
            profile.setFriendship(friendship);
            profile.setFriendRequest(friendRequest);
            searchResult.add(profile);
        }
        return searchResult;
    }
}

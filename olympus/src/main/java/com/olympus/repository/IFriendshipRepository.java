package com.olympus.repository;

import com.olympus.dto.response.friendship.FriendDTO;
import com.olympus.entity.Friendship;
import com.olympus.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface IFriendshipRepository extends JpaRepository<Friendship, Long> {
    @Query("SELECT count (f) from Friendship  f " +
            "where (f.user1 = :user1 and  f.user2 = :user2) " +
            "or (f.user1 = :user2 and  f.user2 = :user1)")
    Long existsFriendship(User user1, User user2);

    @Query("select new com.olympus.dto.response.friendship.FriendDTO(f.id, f.user2.id, f.user2.avatar, f.user2.firstName, f.user2.lastName," +
            "f.user2.birthDate, f.user2.phoneNumber, f.user2.currentAddress, f.user2.occupation, f.user2.gender, f.user2.status) " +
            "from Friendship f where f.user1.id = :userId")
    List<FriendDTO> getListFriendAsSender(Long userId);

    @Query("select new com.olympus.dto.response.friendship.FriendDTO(f.id, f.user1.id, f.user1.avatar, f.user1.firstName, f.user1.lastName," +
            "f.user1.birthDate, f.user1.phoneNumber, f.user1.currentAddress, f.user1.occupation, f.user1.gender, f.user1.status) " +
            "from Friendship f where f.user2.id = :userId")
    List<FriendDTO> getListFriendAsReceiver(Long userId);

    @Query("select count(*) from Friendship f where (f.user1.id = :userId or f.user2.id = :userId) and f.createdTime > :lastWeek")
    Integer countTotalNewFriendsLastWeeks(Long userId, LocalDate lastWeek);

    @Query("select f from Friendship  f where (f.user1.id = :userId1 and f.user2.id = :userId2) or (f.user2.id = :userId1 and f.user1.id = :userId2)")
    Friendship findByUserIds(Long userId1, Long userId2);
}

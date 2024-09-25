package com.olympus.repository;

import com.olympus.dto.response.FriendRequestDTO;
import com.olympus.entity.FriendRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IFriendRequestRepository extends JpaRepository<FriendRequest, Long> {
    @Query("SELECT  count (f) from FriendRequest  f " +
            "where (f.sender.id = :user1 and  f.receiver.id = :user2) " +
            "or (f.receiver.id = :user1 and  f.sender.id = :user2)")
    Long existsFriendRequest(Long user1, Long user2);

    boolean existsByIdAndReceiver_Id(Long requestId, Long userId);
    boolean existsById(long id);

    @Query("select new com.olympus.dto.response.FriendRequestDTO(r.id, r.receiver.id, r.receiver.avatar, " +
            "r.receiver.firstName, r.receiver.lastName, r.receiver.birthDate,r.receiver.phoneNumber, " +
            "r.receiver.currentAddress, r.receiver.occupation, r.receiver.gender, r.receiver.status) from FriendRequest r where r.sender.id = :userId")
    List<FriendRequestDTO> getListRequestSent(Long userId);

    @Query("select new com.olympus.dto.response.FriendRequestDTO(r.id, r.sender.id, r.sender.avatar, " +
            "r.sender.firstName, r.sender.lastName, r.sender.birthDate,r.sender.phoneNumber, " +
            "r.sender.currentAddress, r.sender.occupation, r.sender.gender, r.sender.status) from FriendRequest r where r.receiver.id = :userId")
    List<FriendRequestDTO> getListRequestReceived(Long userId);

    @Query("select count(r) from FriendRequest r where r.id = :requestId and (r.receiver.id = :userId or r.sender.id = :userId)")
    Long checkDeletePermission(Long userId, Long requestId);

    @Query("select r from FriendRequest r where  (r.sender.id = :userId1 and r.receiver.id = :userId2) or (r.receiver.id = :userId1 and r.sender.id = :userId2)")
    FriendRequest findByUserIds(Long userId1, Long userId2);
}

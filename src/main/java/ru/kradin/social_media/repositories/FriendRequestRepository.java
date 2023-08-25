package ru.kradin.social_media.repositories;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import ru.kradin.social_media.models.FriendRequest;

public interface FriendRequestRepository extends JpaRepository<FriendRequest, Long> {
    
    @Query("SELECT f FROM FriendRequest f WHERE f.user.id = :userId")
    Page<FriendRequest> findByUserId(long userId, Pageable pageable);

    @Query("SELECT f FROM FriendRequest f WHERE f.user.id = :userId AND f.requester.id =:requesterId")
    Optional<FriendRequest> findByUserIdAndRequesterId(long userId, long requesterId);
}

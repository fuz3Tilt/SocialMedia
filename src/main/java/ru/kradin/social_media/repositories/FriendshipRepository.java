package ru.kradin.social_media.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import ru.kradin.social_media.models.Friendship;

public interface FriendshipRepository extends JpaRepository<Friendship, Long> {

    @Query("SELECT f FROM Friendship f WHERE f.user1.id = :userId OR f.user2.id = :userId")
    Page<Friendship> findByUserId(long userId, Pageable pageable);

    @Query("SELECT u.id FROM User u WHERE u.id IN (SELECT f.user1.id FROM Friendship f WHERE f.user2.id = :userId) OR u.id IN (SELECT f.user2.id FROM Friendship f WHERE f.user1.id = :userId)")
    List<Long> findFriendIdsByUserId(long userId);

    @Query("SELECT f FROM Friendship f WHERE (f.user1.id = :userId1 AND f.user2.id = :userId2) OR (f.user1.id = :userId2 AND f.user2.id = :userId1)")
    Optional<Friendship> findByUserId1AndUserId2(long userId1, long userId2);
}

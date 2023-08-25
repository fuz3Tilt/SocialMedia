package ru.kradin.social_media.repositories;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import ru.kradin.social_media.models.Followership;

public interface FollowershipRepository extends JpaRepository<Followership, Long> {
    
    @Query("SELECT f FROM Followership f WHERE f.user.id = :userId")
    Page<Followership> findByUserId(long userId, Pageable pageable);
    
    @Query("SELECT f FROM Followership f WHERE f.user.id =:userId AND f.follower.id =:followerId")
    Optional<Followership> findByUserIdAndFollowerId(long userId, long followerId);
}

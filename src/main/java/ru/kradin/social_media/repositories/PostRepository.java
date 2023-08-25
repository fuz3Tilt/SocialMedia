package ru.kradin.social_media.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import ru.kradin.social_media.models.Post;

public interface PostRepository extends JpaRepository<Post, Long> {

    @Query("SELECT p FROM Post p WHERE p.owner.id = :ownerId ORDER BY p.createdAt DESC")
    Page<Post> findByOwnerId(long ownerId, Pageable pageable);

    @Query("SELECT p FROM Post p WHERE p.owner.id IN :userIds ORDER BY p.createdAt DESC")
    Page<Post> findByUsersIdOrderByCreatedAtDesc(List<Long> userIds, Pageable pageable);
}

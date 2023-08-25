package ru.kradin.social_media.repositories;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import ru.kradin.social_media.models.Chat;

public interface ChatRepository extends JpaRepository<Chat, Long> {
    
    @Query("SELECT c FROM Chat c JOIN c.messages m WHERE (c.user1.id = :userId OR c.user2.id = :userId) AND m.wroteAt = (SELECT MAX(m2.wroteAt) FROM Message m2 WHERE m2.chat = c) ORDER BY m.wroteAt DESC")
    Page<Chat> findByUserIdOrderByMessageMaxWroteAt(long userId, Pageable pageable);

    @Query("SELECT c FROM Chat c WHERE (c.user1.id = :userId1 AND c.user2.id = :userId2) OR (c.user1.id = :userId2 AND c.user2.id = :userId1)")
    Optional<Chat> findByUserId1AndUserId2(long userId1, long userId2);
}

package ru.kradin.social_media.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import ru.kradin.social_media.models.Message;

public interface MessageRepository extends JpaRepository<Message, Long> {

    @Query("SELECT m FROM Message m WHERE (m.chat.user1.id = :userId1 AND m.chat.user2.id = :userId2) OR (m.chat.user1.id = :userId2 AND m.chat.user2.id = :userId1) ORDER BY m.wroteAt DESC")
Page<Message> findByUserId1AndUserId2OrderByWroteAtDesc(long userId1, long userId2, Pageable pageable);
}

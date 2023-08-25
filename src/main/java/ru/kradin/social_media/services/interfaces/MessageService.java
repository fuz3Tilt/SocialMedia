package ru.kradin.social_media.services.interfaces;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import ru.kradin.social_media.exceptions.NotFoundException;
import ru.kradin.social_media.models.Message;

public interface MessageService {
    /**
     * Return messages with user
     * @param secondUserId
     * @param pageable
     * @return
     */
    Page<Message> getBySecondUserId(long secondUserId, Pageable pageable);
    void write(String text, long recipientId) throws NotFoundException;
}

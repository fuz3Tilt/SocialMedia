package ru.kradin.social_media.services.interfaces;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import ru.kradin.social_media.exceptions.NoAccessException;
import ru.kradin.social_media.exceptions.NotFoundException;
import ru.kradin.social_media.models.Chat;

public interface ChatService {
    Page<Chat> getAll(Pageable pageable);
    Chat getWithUser(long userId) throws NoAccessException, NotFoundException;
    void delete(long chatId) throws NoAccessException, NotFoundException;
}

package ru.kradin.social_media.services.interfaces;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import ru.kradin.social_media.exceptions.NotFoundException;
import ru.kradin.social_media.models.Friendship;

public interface FriendshipService {
    Page<Friendship> getAll(Pageable pageable);
    void delete(long friendId) throws NotFoundException;
}

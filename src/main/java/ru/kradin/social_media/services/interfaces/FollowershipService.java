package ru.kradin.social_media.services.interfaces;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import ru.kradin.social_media.exceptions.CannotUnsubscribeFromFriendException;
import ru.kradin.social_media.exceptions.NotFoundException;
import ru.kradin.social_media.models.Followership;

public interface FollowershipService {
    Page<Followership> getAll(Pageable pageable);
    void subscribe(long userId) throws NotFoundException;
    void unsubscribe(long userId) throws CannotUnsubscribeFromFriendException, NotFoundException;
}

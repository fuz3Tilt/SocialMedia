package ru.kradin.social_media.services.interfaces;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import ru.kradin.social_media.exceptions.NotFoundException;
import ru.kradin.social_media.models.FriendRequest;

public interface FriendRequestService {
    Page<FriendRequest> getAll(Pageable pageable);
    void accept(long requesterId) throws NotFoundException;
    void cancel(long requesterId) throws NotFoundException;
    void sendRequest(long userId) throws NotFoundException;
    void cancelRequest(long userId) throws NotFoundException;
}

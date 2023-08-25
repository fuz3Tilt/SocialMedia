package ru.kradin.social_media.services.interfaces;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import ru.kradin.social_media.exceptions.NotFoundException;
import ru.kradin.social_media.models.Post;

public interface NonAuthorizePostService {
    Page<Post> getAll(Pageable pageable);
    Post getById(long postId) throws NotFoundException;
}

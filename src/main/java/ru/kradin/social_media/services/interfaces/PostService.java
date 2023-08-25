package ru.kradin.social_media.services.interfaces;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import ru.kradin.social_media.exceptions.NoAccessException;
import ru.kradin.social_media.exceptions.NotFoundException;
import ru.kradin.social_media.models.Post;

public interface PostService {
    Page<Post> getByOwner(long ownerId, Pageable pageable);
    Page<Post> getFeed(Pageable pageable);
    Page<Post> getAll(Pageable pageable);
    Post getById(long postId) throws NotFoundException;
    void create(String title, String text);
    void update(long postId, String title, String text) throws NotFoundException, NoAccessException;
    void delete(long postId) throws NoAccessException, NotFoundException;
}

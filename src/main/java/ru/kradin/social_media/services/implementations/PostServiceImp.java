package ru.kradin.social_media.services.implementations;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ru.kradin.social_media.enums.ErrorMessage;
import ru.kradin.social_media.exceptions.NoAccessException;
import ru.kradin.social_media.exceptions.NotFoundException;
import ru.kradin.social_media.models.Post;
import ru.kradin.social_media.models.User;
import ru.kradin.social_media.repositories.FriendshipRepository;
import ru.kradin.social_media.repositories.PostRepository;
import ru.kradin.social_media.services.interfaces.CurrentSessionService;
import ru.kradin.social_media.services.interfaces.PostService;

@Service
public class PostServiceImp implements PostService {

    @Autowired
    PostRepository postRepository;

    @Autowired
    FriendshipRepository friendshipRepository;

    @Autowired
    CurrentSessionService currentSessionService;

    @Override
    public Page<Post> getAll(Pageable pageable) {
        return postRepository.findAll(pageable);
    }

    @Override
    public Page<Post> getByOwner(long ownerId, Pageable pageable) {
        return postRepository.findByOwnerId(ownerId, pageable);
    }

    @Override
    public Page<Post> getFeed(Pageable pageable) {
        User user = currentSessionService.getUser();
        List<Long> friendIds = friendshipRepository.findFriendIdsByUserId(user.getId());
        return postRepository.findByUsersIdOrderByCreatedAtDesc(friendIds, pageable);
    }

    @Override
    @Transactional
    public void create(String title, String text) {
        User user = currentSessionService.getUser();
        Post post = new Post(title, text, user);
        postRepository.save(post);
    }

    @Override
    @Transactional
    public void update(long postId, String title, String text) throws NotFoundException, NoAccessException {
        User user = currentSessionService.getUser();
        Optional<Post> postOptional = postRepository.findById(postId);
        if (postOptional.isPresent()) {
            Post post = postOptional.get();
            if (!post.getOwner().equals(user)) {
                post.setText(text);
                post.setTitle(title);
                postRepository.save(post);
            } else {
                throw new NoAccessException();
            }
        } else {
            throw new NotFoundException(ErrorMessage.POST_NOT_FOUND);
        }
    }

    @Override
    @Transactional
    public void delete(long postId) throws NoAccessException, NotFoundException {
        User user = currentSessionService.getUser();
        Optional<Post> postOptional = postRepository.findById(postId);
        if (postOptional.isPresent()) {
            Post post = postOptional.get();
            if (!post.getOwner().equals(user)) {
                postRepository.delete(post);
            } else {
                throw new NoAccessException();
            }
        } else {
            throw new NotFoundException(ErrorMessage.POST_NOT_FOUND);
        }
    }

    @Override
    public Post getById(long postId) throws NotFoundException {
        Optional<Post> postOptional = postRepository.findById(postId);
        if (postOptional.isPresent())
            return postOptional.get();
        else
            throw new NotFoundException(ErrorMessage.POST_NOT_FOUND);
    }
    
}

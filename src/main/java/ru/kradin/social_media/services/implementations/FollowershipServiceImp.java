package ru.kradin.social_media.services.implementations;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ru.kradin.social_media.enums.ErrorMessage;
import ru.kradin.social_media.exceptions.CannotUnsubscribeFromFriendException;
import ru.kradin.social_media.exceptions.NotFoundException;
import ru.kradin.social_media.models.Followership;
import ru.kradin.social_media.models.User;
import ru.kradin.social_media.repositories.FollowershipRepository;
import ru.kradin.social_media.repositories.FriendshipRepository;
import ru.kradin.social_media.repositories.UserRepository;
import ru.kradin.social_media.services.interfaces.CurrentSessionService;
import ru.kradin.social_media.services.interfaces.FollowershipService;

@Service
public class FollowershipServiceImp implements FollowershipService{

    @Autowired
    FollowershipRepository followershipRepository;

    @Autowired
    FriendshipRepository friendshipRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    CurrentSessionService currentSessionService;

    @Override
    public Page<Followership> getAll(Pageable pageable) {
        User user = currentSessionService.getUser();
        return followershipRepository.findByUserId(user.getId(), pageable);
    }

    @Override
    @Transactional
    public void subscribe(long userId) throws NotFoundException {
        User follower = currentSessionService.getUser();

        Optional<Followership> followershipOptional = followershipRepository.findByUserIdAndFollowerId(userId, follower.getId());
        if (followershipOptional.isPresent()) {
            return;
        }

        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            Followership followership = new Followership(userOptional.get(), follower);
            followershipRepository.save(followership);
        } else {
            throw new NotFoundException(ErrorMessage.USER_NOT_FOUND);
        }
    }

    @Override
    @Transactional
    public void unsubscribe(long userId) throws NotFoundException, CannotUnsubscribeFromFriendException {
        User follower = currentSessionService.getUser();

        if (friendshipRepository.findByUserId1AndUserId2(follower.getId(), userId).isPresent()) {
            throw new CannotUnsubscribeFromFriendException();
        }

        Optional<Followership> followershipOptional = followershipRepository.findByUserIdAndFollowerId(userId, follower.getId());
        if (followershipOptional.isPresent()) {
            followershipRepository.delete(followershipOptional.get());
        } else {
            return;
        }
    }
    
}

package ru.kradin.social_media.services.implementations;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ru.kradin.social_media.enums.ErrorMessage;
import ru.kradin.social_media.exceptions.NotFoundException;
import ru.kradin.social_media.models.Followership;
import ru.kradin.social_media.models.Friendship;
import ru.kradin.social_media.models.User;
import ru.kradin.social_media.repositories.FollowershipRepository;
import ru.kradin.social_media.repositories.FriendshipRepository;
import ru.kradin.social_media.services.interfaces.CurrentSessionService;
import ru.kradin.social_media.services.interfaces.FriendshipService;

@Service
public class FriendshipServiceImp implements FriendshipService{

    @Autowired
    FriendshipRepository friendshipRepository;

    @Autowired
    FollowershipRepository followershipRepository;

    @Autowired
    CurrentSessionService currentSessionService;

    @Override
    public Page<Friendship> getAll(Pageable pageable) {
        User user = currentSessionService.getUser();
        return friendshipRepository.findByUserId(user.getId(), pageable);
    }

    @Override
    @Transactional
    public void delete(long friendId) throws NotFoundException {
        User user = currentSessionService.getUser();
        Optional<Friendship> friendshipOptional = friendshipRepository.findByUserId1AndUserId2(user.getId(), friendId);
        if (friendshipOptional.isPresent()) {
            Optional<Followership> followershipOptional = followershipRepository.findByUserIdAndFollowerId(friendId, user.getId());
            followershipRepository.delete(followershipOptional.get());
            friendshipRepository.deleteById(friendshipOptional.get().getId());
        } else {
            throw new NotFoundException(ErrorMessage.FRIENDSHIP_NOT_FOUND);
        }
    }
    
}

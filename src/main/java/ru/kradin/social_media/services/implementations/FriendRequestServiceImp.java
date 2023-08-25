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
import ru.kradin.social_media.models.FriendRequest;
import ru.kradin.social_media.models.Friendship;
import ru.kradin.social_media.models.User;
import ru.kradin.social_media.repositories.FollowershipRepository;
import ru.kradin.social_media.repositories.FriendRequestRepository;
import ru.kradin.social_media.repositories.FriendshipRepository;
import ru.kradin.social_media.repositories.UserRepository;
import ru.kradin.social_media.services.interfaces.CurrentSessionService;
import ru.kradin.social_media.services.interfaces.FriendRequestService;

@Service
public class FriendRequestServiceImp implements FriendRequestService {

    @Autowired
    FriendRequestRepository friendRequestRepository;

    @Autowired
    FriendshipRepository friendshipRepository;

    @Autowired
    FollowershipRepository followershipRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    CurrentSessionService currentSessionService;

    @Override
    public Page<FriendRequest> getAll(Pageable pageable) {
        User user = currentSessionService.getUser();
        return friendRequestRepository.findByUserId(user.getId(), pageable);
    }

    @Override
    @Transactional
    public void accept(long requesterId) throws NotFoundException {
        User user1 = currentSessionService.getUser();
        Optional<FriendRequest> friendRequestOptional = friendRequestRepository.findByUserIdAndRequesterId(user1.getId(), requesterId);
        if (friendRequestOptional.isPresent()) {
            FriendRequest friendRequest = friendRequestOptional.get();
            friendRequestRepository.delete(friendRequest);

            User user2 = friendRequest.getRequester();

            Friendship friendship = new Friendship(user1, user2);
            friendshipRepository.save(friendship);

            Optional<Followership> followershipOptional1 = followershipRepository.findByUserIdAndFollowerId(user1.getId(), requesterId);
            if (followershipOptional1.isEmpty()) {
                Followership followership1 = new Followership(user1, user2);
                followershipRepository.save(followership1);
            }
            Followership followership2 = new Followership(user2, user1);
            followershipRepository.save(followership2);
        } else {
            throw new NotFoundException(ErrorMessage.FRIEND_REQUEST_NOT_FOUND);
        }
    }

    @Override
    @Transactional
    public void cancel(long requesterId) throws NotFoundException {
        User user = currentSessionService.getUser();
        Optional<FriendRequest> friendRequestOptional = friendRequestRepository.findByUserIdAndRequesterId(user.getId(), requesterId);
        if (friendRequestOptional.isPresent()) {
            friendRequestRepository.delete(friendRequestOptional.get());
        } else {
            throw new NotFoundException(ErrorMessage.FRIEND_REQUEST_NOT_FOUND);
        }
    }

    @Override
    @Transactional
    public void sendRequest(long userId) throws NotFoundException {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            throw new NotFoundException(ErrorMessage.USER_NOT_FOUND);
        }
        User friendRequester = currentSessionService.getUser();
        FriendRequest friendRequest = new FriendRequest(userOptional.get(), friendRequester);
        friendRequestRepository.save(friendRequest);
    }

    @Override
    @Transactional
    public void cancelRequest(long userId) throws NotFoundException {
        User friendRequester = currentSessionService.getUser();
        Optional<FriendRequest> friendRequestOptional = friendRequestRepository.findByUserIdAndRequesterId(userId, friendRequester.getId());
        if (friendRequestOptional.isPresent()) {
            friendRequestRepository.delete(friendRequestOptional.get());
        } else {
            throw new NotFoundException(ErrorMessage.FRIEND_REQUEST_NOT_FOUND);
        }
    }
    
}

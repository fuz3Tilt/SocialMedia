package ru.kradin.social_media.services.implementations;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ru.kradin.social_media.enums.ErrorMessage;
import ru.kradin.social_media.exceptions.NoAccessException;
import ru.kradin.social_media.exceptions.NotFoundException;
import ru.kradin.social_media.models.Chat;
import ru.kradin.social_media.models.User;
import ru.kradin.social_media.repositories.ChatRepository;
import ru.kradin.social_media.repositories.FriendshipRepository;
import ru.kradin.social_media.repositories.UserRepository;
import ru.kradin.social_media.services.interfaces.ChatService;
import ru.kradin.social_media.services.interfaces.CurrentSessionService;

@Service
public class ChatServiceImp implements ChatService {

    @Autowired
    ChatRepository chatRepository;

    @Autowired
    FriendshipRepository friendshipRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    CurrentSessionService currentSessionService;

    @Override
    public Page<Chat> getAll(Pageable pageable) {
        User user = currentSessionService.getUser();
        return chatRepository.findByUserIdOrderByMessageMaxWroteAt(user.getId(), pageable);
    }

    @Override
    @Transactional
    public Chat getWithUser(long userId) throws NotFoundException, NoAccessException {
        User user = currentSessionService.getUser();
        Optional<Chat> chatOptional = chatRepository.findByUserId1AndUserId2(user.getId(), userId);
        if (chatOptional.isPresent()) {
            return chatOptional.get();
        } else {
            if (friendshipRepository.findByUserId1AndUserId2(user.getId(), userId).isEmpty()) {
                throw new NoAccessException();
            }
            Optional<User> userOptional = userRepository.findById(userId);
            if (userOptional.isPresent()) {
                Chat newChat = new Chat(currentSessionService.getUser(), userOptional.get());
                newChat = chatRepository.save(newChat);
                return newChat;
            } else {
                throw new NotFoundException(ErrorMessage.USER_NOT_FOUND);
            }
        }
    }

    @Override
    @Transactional
    public void delete(long chatId) throws NoAccessException, NotFoundException {
        User user = currentSessionService.getUser();
        Optional<Chat> chatOptional = chatRepository.findById(chatId);
        if (chatOptional.isPresent()) {
            Chat chat = chatOptional.get();
            if (chat.getUser1().equals(user) || chat.getUser2().equals(user)) {
                chatRepository.deleteById(chatId);
            } else {
                throw new NoAccessException();
            }
        } else {
            throw new NotFoundException(ErrorMessage.CHAT_NOT_FOUND);
        }
    }
    
}

package ru.kradin.social_media.services.implementations;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ru.kradin.social_media.enums.ErrorMessage;
import ru.kradin.social_media.exceptions.NotFoundException;
import ru.kradin.social_media.models.Chat;
import ru.kradin.social_media.models.Message;
import ru.kradin.social_media.models.User;
import ru.kradin.social_media.repositories.ChatRepository;
import ru.kradin.social_media.repositories.MessageRepository;
import ru.kradin.social_media.services.interfaces.CurrentSessionService;
import ru.kradin.social_media.services.interfaces.MessageService;

@Service
public class MessageServiceImp implements MessageService{
    
    @Autowired
    MessageRepository messageRepository;

    @Autowired
    ChatRepository chatRepository;

    @Autowired
    CurrentSessionService currentSessionService;

    @Override
    public Page<Message> getBySecondUserId(long secondUserId, Pageable pageable) {
        User user = currentSessionService.getUser();
        return messageRepository.findByUserId1AndUserId2OrderByWroteAtDesc(user.getId(), secondUserId, pageable);
    }

    @Override
    @Transactional
    public void write(String text, long recipientId) throws NotFoundException {
        User user = currentSessionService.getUser();
        Optional<Chat> chatOptional = chatRepository.findByUserId1AndUserId2(recipientId, recipientId);
        if (chatOptional.isPresent()) {
            Message message = new Message(text, user, chatOptional.get());
            messageRepository.save(message);
        } else {
            throw new NotFoundException(ErrorMessage.CHAT_NOT_FOUND);
        }
    }
}

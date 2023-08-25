package ru.kradin.social_media.responses;

import java.util.List;

import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import ru.kradin.social_media.models.Chat;

public class ChatPageResponse extends PageImpl<Chat> {
    
    public ChatPageResponse(List<Chat> content, Pageable pageable, long total) {
        super(content, pageable, total);
    }
    
}

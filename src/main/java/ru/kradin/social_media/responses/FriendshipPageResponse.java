package ru.kradin.social_media.responses;

import java.util.List;

import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import ru.kradin.social_media.models.Friendship;

public class FriendshipPageResponse extends PageImpl<Friendship> {
    
    public FriendshipPageResponse(List<Friendship> content, Pageable pageable, long total) {
        super(content, pageable, total);
    }

}

package ru.kradin.social_media.responses;

import java.util.List;

import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import ru.kradin.social_media.models.FriendRequest;

public class FriendRequestPageResponse extends PageImpl<FriendRequest> {
    
    public FriendRequestPageResponse(List<FriendRequest> content, Pageable pageable, long total) {
        super(content, pageable, total);
    }

}

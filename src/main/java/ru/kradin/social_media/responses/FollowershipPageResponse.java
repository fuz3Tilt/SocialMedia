package ru.kradin.social_media.responses;

import java.util.List;

import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import ru.kradin.social_media.models.Followership;

public class FollowershipPageResponse extends PageImpl<Followership> {

    public FollowershipPageResponse(List<Followership> content, Pageable pageable, long total) {
        super(content, pageable, total);
    }
    
}

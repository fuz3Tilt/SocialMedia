package ru.kradin.social_media.responses;

import java.util.List;

import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import ru.kradin.social_media.models.Post;

public class PostPageResponse extends PageImpl<Post> {
    
    public PostPageResponse(List<Post> content, Pageable pageable, long total) {
        super(content, pageable, total);
    }

}

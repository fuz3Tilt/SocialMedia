package ru.kradin.social_media.services.interfaces;

import ru.kradin.social_media.models.User;

public interface CurrentSessionService {
    User getUser();
}

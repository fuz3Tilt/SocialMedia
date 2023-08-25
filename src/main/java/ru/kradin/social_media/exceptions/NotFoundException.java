package ru.kradin.social_media.exceptions;

import ru.kradin.social_media.enums.ErrorMessage;

public class NotFoundException extends Exception {
    
    public NotFoundException(ErrorMessage errorMessage) {
        super(errorMessage.getText());
    }
}

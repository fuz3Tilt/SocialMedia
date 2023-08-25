package ru.kradin.social_media.DTOs;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class PostDTO {
    @NotBlank(message = "Title must not be blank")
    @NotNull(message = "Title must not be null")
    private String title;
    @NotBlank(message = "Text must not be blank")
    @NotNull(message = "Text must not be null")
    private String text;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }
}

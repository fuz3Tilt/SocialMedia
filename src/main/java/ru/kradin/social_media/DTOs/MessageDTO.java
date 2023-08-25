package ru.kradin.social_media.DTOs;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

public class MessageDTO {
    @NotBlank(message = "Text must not be blank")
    private String text;
    @NotNull(message = "Recipient ID must not be null")
    @Positive(message = "Recipient ID must be greater than 0")
    private long recipientId;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public long getRecipientId() {
        return recipientId;
    }

    public void setRecipientId(long recipientId) {
        this.recipientId = recipientId;
    }
}

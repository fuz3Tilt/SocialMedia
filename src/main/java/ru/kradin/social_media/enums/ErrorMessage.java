package ru.kradin.social_media.enums;

public enum ErrorMessage {
    POST_NOT_FOUND("Post not found."),
    FRIENDSHIP_NOT_FOUND("Friendship not found."),
    USER_NOT_FOUND("User not found."),
    CHAT_NOT_FOUND("Chat not found."),
    SUBSCRIPTION_NOT_FOUND("Subscription not found."),
    FRIEND_REQUEST_NOT_FOUND("Friend request not found.");
    
    private final String text;
    
    ErrorMessage(String text) {
        this.text = text;
    }
    
    public String getText() {
        return text;
    }
}

package ru.kradin.social_media.models;

import java.time.LocalDateTime;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.springframework.data.jpa.domain.AbstractPersistable;

@Entity
public class Message extends AbstractPersistable<Long> {
    @Column(nullable = false)
    private String text;
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(nullable = false)
    private User owner;
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(nullable = false)
    private Chat chat;
    @Column(nullable = false, columnDefinition = "TIMESTAMP")
    private LocalDateTime wroteAt = LocalDateTime.now();

    public Message() {
    }

    public Message(String text, User owner, Chat chat) {
        this.text = text;
        this.owner = owner;
        this.chat = chat;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public LocalDateTime getWroteAt() {
        return wroteAt;
    }

    public void setWroteAt(LocalDateTime wroteAt) {
        this.wroteAt = wroteAt;
    }

    public Chat getChat() {
        return chat;
    }
}

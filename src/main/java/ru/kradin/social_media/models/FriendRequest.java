package ru.kradin.social_media.models;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.springframework.data.jpa.domain.AbstractPersistable;

@Entity
public class FriendRequest extends AbstractPersistable<Long> {
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(nullable = false)
    private User user;
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(nullable = false)
    private User requester;

    public FriendRequest() {
    }

    public FriendRequest(User user, User requester) {
        this.user = user;
        this.requester = requester;
    }
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getRequester() {
        return requester;
    }

    public void setRequester(User requester) {
        this.requester = requester;
    }
}

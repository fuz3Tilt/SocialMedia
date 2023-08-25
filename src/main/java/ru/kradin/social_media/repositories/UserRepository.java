package ru.kradin.social_media.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import ru.kradin.social_media.models.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

}
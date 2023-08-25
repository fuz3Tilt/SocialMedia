package ru.kradin.social_media.services.implementations;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import ru.kradin.social_media.models.User;
import ru.kradin.social_media.repositories.UserRepository;
import ru.kradin.social_media.services.interfaces.CurrentSessionService;

@Service
public class CurrentSessionServiceImp implements CurrentSessionService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public User getUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername();

        Optional<User> user = userRepository.findByUsername(username);

        return user.get();
    }
}

package ru.kradin.social_media.services.implementations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.kradin.social_media.models.User;
import ru.kradin.social_media.repositories.UserRepository;
import ru.kradin.social_media.security.UserDetailsImp;

import java.util.Optional;

@Service
public class UserDetailsServiceImp implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByUsername(username);

        if(user.isEmpty())
            throw new UsernameNotFoundException("User not found");

        return new UserDetailsImp(user.get());
    }
}

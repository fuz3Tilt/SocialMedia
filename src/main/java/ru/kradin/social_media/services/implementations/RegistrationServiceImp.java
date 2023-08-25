package ru.kradin.social_media.services.implementations;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ru.kradin.social_media.DTOs.RegistrationDTO;
import ru.kradin.social_media.enums.Role;
import ru.kradin.social_media.models.User;
import ru.kradin.social_media.repositories.UserRepository;
import ru.kradin.social_media.services.interfaces.RegistrationService;

@Service
public class RegistrationServiceImp implements RegistrationService{
    private static final Logger log = LoggerFactory.getLogger(RegistrationServiceImp.class);


    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void register(RegistrationDTO registrationDTO) {
        User newUser = new User(
            registrationDTO.getEmail(),
             registrationDTO.getUsername(),
              passwordEncoder.encode(registrationDTO.getPassword()),
              Role.ROLE_USER);

              userRepository.save(newUser);

        log.info("User {} created", newUser.getUsername());
    }
}

package ru.kradin.social_media.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import ru.kradin.social_media.DTOs.RegistrationDTO;
import ru.kradin.social_media.models.User;
import ru.kradin.social_media.repositories.UserRepository;

import java.util.Optional;
/*
    Class for checking user data for uniqueness
 */
@Component
public class UserValidator implements Validator {

    @Autowired
    private UserRepository userRepository;

    @Override
    public boolean supports(Class<?> aClass) {
        return RegistrationDTO.class.equals(aClass);
    }

    //Add field errors about not unique data into BindingResult
    @Override
    public void validate(Object o, Errors errors) {
        RegistrationDTO userRegistrationDTO = (RegistrationDTO) o;

        Optional<User> userWithTheSameEmail = userRepository.findByEmail(userRegistrationDTO.getEmail());
        if (userWithTheSameEmail.isPresent())
            errors.rejectValue("email", "", "Email is already taken");

        Optional<User> userWithTheSameUsername = userRepository.findByUsername(userRegistrationDTO.getUsername());
        if (userWithTheSameUsername.isPresent())
            errors.rejectValue("username", "", "Username is already taken");
    }
}

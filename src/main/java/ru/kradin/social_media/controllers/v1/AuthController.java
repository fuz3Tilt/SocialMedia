package ru.kradin.social_media.controllers.v1;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import ru.kradin.social_media.DTOs.CredentialsDTO;
import ru.kradin.social_media.DTOs.RegistrationDTO;
import ru.kradin.social_media.responses.JWTResponse;
import ru.kradin.social_media.security.JWTUtil;
import ru.kradin.social_media.services.interfaces.RegistrationService;
import ru.kradin.social_media.utils.UserValidator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/auth")
@Api(value = "Authentication API", tags = {"Authentication"})
public class AuthController {

    @Autowired
    private RegistrationService registrationService;
    @Autowired
    private UserValidator userValidator;
    @Autowired
    private JWTUtil jwtUtil;
    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/registration")
    @ApiOperation(value = "Perform user registration", notes = "Register a new user and return JWT token")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "User registered successfully", response = JWTResponse.class),
            @ApiResponse(code = 400, message = "Validation error", response = BindingResult.class)
    })
    public ResponseEntity<?> performRegistration(@RequestBody @Valid RegistrationDTO registrationDTO,
                                                 BindingResult bindingResult) {

        userValidator.validate(registrationDTO, bindingResult);
        if (bindingResult.hasErrors())
            return ResponseEntity.badRequest().body(bindingResult);
        registrationService.register(registrationDTO);

        String token = jwtUtil.generateToken(registrationDTO.getUsername());
        return ResponseEntity.status(HttpStatus.CREATED).body(new JWTResponse(token));
    }

    @PostMapping("/login")
    @ApiOperation(value = "Perform user login", notes = "Authenticate user and return JWT token")
    @ApiResponses(value = {
            @ApiResponse(code = 200,  message = "User authenticated successfully", response = JWTResponse.class),
            @ApiResponse(code = 401, message = "Incorrect credentials")
    })
    public ResponseEntity<?> performLogin(@RequestBody CredentialsDTO credentialsDTO) {
        UsernamePasswordAuthenticationToken authInputToken =
                new UsernamePasswordAuthenticationToken(credentialsDTO.getUsername(),
                        credentialsDTO.getPassword());

        try {
            authenticationManager.authenticate(authInputToken);
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String token = jwtUtil.generateToken(credentialsDTO.getUsername());

        return ResponseEntity.ok(new JWTResponse(token));
    }
}

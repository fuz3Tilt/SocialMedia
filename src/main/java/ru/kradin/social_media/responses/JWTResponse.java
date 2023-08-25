package ru.kradin.social_media.responses;

import io.swagger.annotations.ApiModel;

@ApiModel
public class JWTResponse {
    private final String JWT;

    public JWTResponse(String JWT) {
        this.JWT = JWT;
    }
    public String getJWT() {
        return JWT;
    }
}

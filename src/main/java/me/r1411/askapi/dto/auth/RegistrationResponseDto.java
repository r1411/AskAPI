package me.r1411.askapi.dto.auth;

import java.util.Date;

public record RegistrationResponseDto(String token, Date expirationDate) {
}

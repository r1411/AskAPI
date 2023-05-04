package me.r1411.askapi.dto.auth;

import jakarta.validation.constraints.NotNull;

public record AuthenticationRequestDto(
        @NotNull(message = "Missing field: username")
        String username,

        @NotNull(message = "Missing field: password")
        String password) {
}

package me.r1411.askapi.dto.auth;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationRequestDto {
    @NotNull(message = "Missing field: username")
    private String username;

    @NotNull(message = "Missing field: password")
    private String password;
}

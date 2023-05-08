package me.r1411.askapi.dto.auth;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.r1411.askapi.validator.UniqueUsernameConstraint;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegistrationRequestDto {
    @UniqueUsernameConstraint
    @NotNull(message = "Missing field: username")
    @Size(min = 4, max = 32, message = "Username length should be between 4 and 32 characters long")
    @Pattern(regexp = "^[a-zA-Z0-9_]+$", message = "Username can only contain numbers, letters and underscores")
    private String username;

    @NotNull(message = "Missing field: password")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    private String password;
}

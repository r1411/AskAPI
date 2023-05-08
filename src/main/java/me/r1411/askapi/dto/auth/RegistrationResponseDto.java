package me.r1411.askapi.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegistrationResponseDto {
    private String token;

    private Date expirationDate;
}

package me.r1411.askapi.dto.error;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponseDto {
    private String message;

    private Date timestamp;

    public ErrorResponseDto(String message) {
        this(message, new Date());
    }
}

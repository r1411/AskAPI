package me.r1411.askapi.dto.error;

import java.util.Date;

public record ErrorResponseDto(String message, Date timestamp) {
    public ErrorResponseDto(String message) {
        this(message, new Date());
    }
}

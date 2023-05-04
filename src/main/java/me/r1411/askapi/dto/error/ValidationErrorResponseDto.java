package me.r1411.askapi.dto.error;

import java.util.Date;
import java.util.Map;

public record ValidationErrorResponseDto(String message, Date timestamp, Map<String, String> invalidParams) {
    public ValidationErrorResponseDto(String message, Map<String, String> invalidParams) {
        this(message, new Date(), invalidParams);
    }
}

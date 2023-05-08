package me.r1411.askapi.dto.error;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.Map;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class ValidationErrorResponseDto {
    private String message;

    private Date timestamp;

    private Map<String, String> invalidParams;

    public ValidationErrorResponseDto(String message, Map<String, String> invalidParams) {
        this(message, new Date(), invalidParams);
    }
}

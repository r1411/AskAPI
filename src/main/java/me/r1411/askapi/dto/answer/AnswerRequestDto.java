package me.r1411.askapi.dto.answer;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnswerRequestDto {
    @NotNull(message = "Text should not be empty")
    @Size(min = 2, max = 4000, message = "Text length should be between 2 and 4000 characters long")
    private String text;
}

package me.r1411.askapi.dto.question;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuestionCreateRequestDto {
    @NotNull(message = "Missing field: title")
    @Size(min = 2, max = 128, message = "Title length should be between 2 and 128 characters long")
    private String title;

    @NotNull(message = "Missing field: text")
    @Size(min = 2, max = 4000, message = "Text length should be between 2 and 4000 characters long")
    private String text;

    @NotNull(message = "Missing field: board_id")
    private Integer boardId;
}

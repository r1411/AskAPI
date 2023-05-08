package me.r1411.askapi.dto.board;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BoardRequestDto {
    @NotNull(message = "Missing field: title")
    @Size(min = 2, max = 32, message = "Title length should be between 2 and 32 characters long")
    private String title;
}

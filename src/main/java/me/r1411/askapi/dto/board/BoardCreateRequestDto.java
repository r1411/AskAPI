package me.r1411.askapi.dto.board;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import me.r1411.askapi.model.Board;

public record BoardCreateRequestDto(
        @NotNull(message = "Missing field: title")
        @Size(min = 2, max = 32, message = "Title length should be between 2 and 32 characters long")
        String title) {
    public Board toBoard() {
        return new Board(title());
    }
}

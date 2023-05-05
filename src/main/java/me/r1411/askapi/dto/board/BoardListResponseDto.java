package me.r1411.askapi.dto.board;

import java.util.List;

public record BoardListResponseDto(int count, List<BoardResponseDto> items) {
    public BoardListResponseDto(List<BoardResponseDto> items) {
        this(items.size(), items);
    }
}

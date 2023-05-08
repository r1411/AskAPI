package me.r1411.askapi.dto.board;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BoardListResponseDto {
    private int count;

    private List<BoardResponseDto> items;

    public BoardListResponseDto(List<BoardResponseDto> items) {
        this(items.size(), items);
    }
}

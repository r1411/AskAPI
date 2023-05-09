package me.r1411.askapi.dto.answer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnswerListPageResponseDto {
    private int page;
    private int pagesCount;
    private List<AnswerResponseDto> items;
}

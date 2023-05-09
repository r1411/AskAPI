package me.r1411.askapi.dto.question;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuestionListPageResponseDto {
    private int page;
    private int pagesCount;
    private List<QuestionResponseDto> items;
}

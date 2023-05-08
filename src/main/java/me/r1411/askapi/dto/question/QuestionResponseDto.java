package me.r1411.askapi.dto.question;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.r1411.askapi.dto.board.BoardResponseDto;
import me.r1411.askapi.dto.user.UserResponseDto;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuestionResponseDto {
    private int id;

    private String title;

    private String text;

    private Date createdAt;

    private BoardResponseDto board;

    private UserResponseDto author;
}

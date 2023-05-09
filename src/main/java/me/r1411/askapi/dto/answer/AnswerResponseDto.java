package me.r1411.askapi.dto.answer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.r1411.askapi.dto.user.UserResponseDto;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnswerResponseDto {
    private int id;
    private UserResponseDto user;
    private String text;
    private Date createdAt;
}

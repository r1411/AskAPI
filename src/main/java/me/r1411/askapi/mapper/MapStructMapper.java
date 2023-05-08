package me.r1411.askapi.mapper;

import me.r1411.askapi.dto.auth.RegistrationRequestDto;
import me.r1411.askapi.dto.board.BoardRequestDto;
import me.r1411.askapi.dto.board.BoardResponseDto;
import me.r1411.askapi.model.Board;
import me.r1411.askapi.model.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MapStructMapper {
    User registrationRequestToUser(RegistrationRequestDto registrationRequestDto);

    Board boardRequestToBoard(BoardRequestDto boardRequestDto);

    BoardResponseDto boardToBoardResponse(Board board);
}

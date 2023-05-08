package me.r1411.askapi.mapper;

import me.r1411.askapi.dto.auth.RegistrationRequestDto;
import me.r1411.askapi.dto.board.BoardRequestDto;
import me.r1411.askapi.dto.board.BoardResponseDto;
import me.r1411.askapi.dto.question.QuestionCreateRequestDto;
import me.r1411.askapi.dto.question.QuestionResponseDto;
import me.r1411.askapi.dto.question.QuestionUpdateRequestDto;
import me.r1411.askapi.dto.user.UserResponseDto;
import me.r1411.askapi.model.Board;
import me.r1411.askapi.model.Question;
import me.r1411.askapi.model.User;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedSourcePolicy = ReportingPolicy.ERROR,
        unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface MapStructMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "role", ignore = true)
    User registrationRequestToUser(RegistrationRequestDto registrationRequestDto);

    @Mapping(target = "id", ignore = true)
    Board boardRequestToBoard(BoardRequestDto boardRequestDto);

    BoardResponseDto boardToBoardResponse(Board board);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "author", ignore = true)
    @Mapping(target = "board", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @BeanMapping(ignoreUnmappedSourceProperties = {"boardId"})
    Question questionCreateRequestToQuestion(QuestionCreateRequestDto questionCreateRequestDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "author", ignore = true)
    @Mapping(target = "board", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    Question questionUpdateRequestToQuestion(QuestionUpdateRequestDto questionCreateRequestDto);

    QuestionResponseDto questionToQuestionResponse(Question question);

    @BeanMapping(ignoreUnmappedSourceProperties = {"password", "role"})
    UserResponseDto userToUserResponse(User user);
}

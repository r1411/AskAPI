package me.r1411.askapi.controller.exception;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import me.r1411.askapi.controller.wrapper.ErrorResponseEntity;
import me.r1411.askapi.dto.error.ErrorResponseDto;
import me.r1411.askapi.dto.error.ValidationErrorResponseDto;
import me.r1411.askapi.exception.ObjectNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class ApiExceptionHandler {
    @ExceptionHandler
    public ErrorResponseEntity<ErrorResponseDto> noHandlerException(NoHandlerFoundException e) {
        return new ErrorResponseEntity<>(new ErrorResponseDto("Endpoint not found"), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ErrorResponseEntity<ErrorResponseDto> objectNotFoundException(ObjectNotFoundException e) {
        return new ErrorResponseEntity<>(new ErrorResponseDto(e.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ErrorResponseEntity<ValidationErrorResponseDto> validationException(MethodArgumentNotValidException e) {
        return new ErrorResponseEntity<>(new ValidationErrorResponseDto("Validation failed", getValidationErrors(e.getBindingResult())), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ErrorResponseEntity<ErrorResponseDto> constraintViolation(ConstraintViolationException e) {
        String msg = e.getConstraintViolations().stream().findFirst()
                .map(ConstraintViolation::getMessage)
                .orElse("Invalid request");

        return new ErrorResponseEntity<>(new ErrorResponseDto(msg), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ErrorResponseEntity<ErrorResponseDto> missingRequestParam(MissingServletRequestParameterException e) {
        return new ErrorResponseEntity<>(new ErrorResponseDto(e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ErrorResponseEntity<ErrorResponseDto> wrongArgumentType(MethodArgumentTypeMismatchException e) {
        String msg = "Param " + e.getPropertyName() + " should be " + e.getRequiredType();
        return new ErrorResponseEntity<>(new ErrorResponseDto(msg), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ErrorResponseEntity<ErrorResponseDto> accessDeniedException(AccessDeniedException e) {
        return new ErrorResponseEntity<>(new ErrorResponseDto("You don't have permission to perform this action"), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler
    public ErrorResponseEntity<ErrorResponseDto> methodNotSupported(HttpRequestMethodNotSupportedException e) {
        return new ErrorResponseEntity<>(new ErrorResponseDto("Method not supported"), HttpStatus.METHOD_NOT_ALLOWED);
    }

    @ExceptionHandler(Exception.class)
    private ErrorResponseEntity<ErrorResponseDto> otherException(Exception e) {
        return new ErrorResponseEntity<>(new ErrorResponseDto("Internal server error"), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private Map<String, String> getValidationErrors(BindingResult bindingResult) {
        return bindingResult.getFieldErrors().stream()
                .collect(Collectors.toMap(
                                FieldError::getField,
                                err -> (err.getDefaultMessage() == null) ? "Invalid value" : err.getDefaultMessage(),
                                (err1, err2) -> err1
                        )
                );
    }
}

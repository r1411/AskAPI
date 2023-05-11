package me.r1411.askapi.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import me.r1411.askapi.controller.wrapper.ErrorResponseEntity;
import me.r1411.askapi.controller.wrapper.SuccessResponseEntity;
import me.r1411.askapi.dto.auth.AuthenticationRequestDto;
import me.r1411.askapi.dto.auth.AuthenticationResponseDto;
import me.r1411.askapi.dto.auth.RegistrationRequestDto;
import me.r1411.askapi.dto.auth.RegistrationResponseDto;
import me.r1411.askapi.dto.error.ErrorResponseDto;
import me.r1411.askapi.mapper.MapStructMapper;
import me.r1411.askapi.model.User;
import me.r1411.askapi.security.jwt.JwtUtils;
import me.r1411.askapi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Map;


@Tag(name = "auth", description = "Регистрация, аутентификация")
@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthenticationManager authenticationManager;

    private final JwtUtils jwtUtils;

    private final UserService userService;

    private final MapStructMapper mapper;


    @Autowired
    public AuthController(AuthenticationManager authenticationManager, JwtUtils jwtUtils, UserService userService, MapStructMapper mapper) {
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
        this.userService = userService;
        this.mapper = mapper;
    }

    @Operation(
            operationId = "login",
            summary = "Войти в существующий акканунт"
    )
    @ResponseBody
    @PostMapping("/login")
    public SuccessResponseEntity<AuthenticationResponseDto> login(@RequestBody @Valid AuthenticationRequestDto requestDto) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(requestDto.getUsername(), requestDto.getPassword()));
            User user = userService.findByUsername(requestDto.getUsername()).orElseThrow(() -> new IllegalStateException("User not found, but authentication is completed"));
            Map.Entry<String, Date> tokenInfo = jwtUtils.createToken(user);
            String token = tokenInfo.getKey();
            Date expirationDate = tokenInfo.getValue();
            return new SuccessResponseEntity<>(new AuthenticationResponseDto(token, expirationDate), HttpStatus.OK);
        } catch (AuthenticationException e) {
            throw new BadCredentialsException("Invalid username or password");
        }
    }

    @Operation(
            operationId = "register",
            summary = "Зарегистрировать новый аккаунт"
    )
    @ResponseBody
    @PostMapping("/register")
    public SuccessResponseEntity<RegistrationResponseDto> register(@RequestBody @Valid RegistrationRequestDto requestDto) {
        User createdUser = userService.register(mapper.registrationRequestToUser(requestDto));
        Map.Entry<String, Date> tokenInfo = jwtUtils.createToken(createdUser);
        String token = tokenInfo.getKey();
        Date expirationDate = tokenInfo.getValue();
        return new SuccessResponseEntity<>(new RegistrationResponseDto(token, expirationDate), HttpStatus.OK);
    }

    @ExceptionHandler(BadCredentialsException.class)
    private ErrorResponseEntity<ErrorResponseDto> handleBadCredentialsException(BadCredentialsException e) {
        return new ErrorResponseEntity<>(new ErrorResponseDto(e.getMessage()), HttpStatus.UNAUTHORIZED);
    }
}

package me.r1411.askapi.controller;

import jakarta.validation.Valid;
import me.r1411.askapi.controller.wrapper.ErrorResponseEntity;
import me.r1411.askapi.controller.wrapper.SuccessResponseEntity;
import me.r1411.askapi.dto.auth.AuthenticationRequestDto;
import me.r1411.askapi.dto.auth.AuthenticationResponseDto;
import me.r1411.askapi.dto.auth.RegistrationRequestDto;
import me.r1411.askapi.dto.auth.RegistrationResponseDto;
import me.r1411.askapi.dto.error.ErrorResponseDto;
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


@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthenticationManager authenticationManager;

    private final JwtUtils jwtUtils;

    private final UserService userService;


    @Autowired
    public AuthController(AuthenticationManager authenticationManager, JwtUtils jwtUtils, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
        this.userService = userService;
    }

    @ResponseBody
    @PostMapping("/login")
    public SuccessResponseEntity<AuthenticationResponseDto> login(@RequestBody @Valid AuthenticationRequestDto requestDto) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(requestDto.username(), requestDto.password()));
            User user = userService.findByUsername(requestDto.username()).orElseThrow(() -> new IllegalStateException("User not found, but authentication is completed"));
            Map.Entry<String, Date> tokenInfo = jwtUtils.createToken(user);
            String token = tokenInfo.getKey();
            Date expirationDate = tokenInfo.getValue();
            return new SuccessResponseEntity<>(new AuthenticationResponseDto(token, expirationDate), HttpStatus.OK);
        } catch (AuthenticationException e) {
            throw new BadCredentialsException("Invalid username or password");
        }
    }

    @ResponseBody
    @PostMapping("/register")
    public SuccessResponseEntity<RegistrationResponseDto> register(@RequestBody @Valid RegistrationRequestDto requestDto) {
        User createdUser = userService.register(requestDto.toUser());
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

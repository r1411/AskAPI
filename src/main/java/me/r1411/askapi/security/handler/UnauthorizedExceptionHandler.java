package me.r1411.askapi.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import me.r1411.askapi.dto.error.ErrorResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class UnauthorizedExceptionHandler implements AuthenticationEntryPoint {
    private final ObjectMapper mapper;

    @Autowired
    public UnauthorizedExceptionHandler(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        ErrorResponseDto error = new ErrorResponseDto(authException.getMessage());

        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        ObjectNode root = mapper.createObjectNode();
        root.put("success", false);
        root.set("error", mapper.valueToTree(error));

        mapper.writeValue(response.getOutputStream(), root);
    }
}

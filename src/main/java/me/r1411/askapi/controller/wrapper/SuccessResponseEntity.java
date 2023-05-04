package me.r1411.askapi.controller.wrapper;

import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import java.util.Map;

public class SuccessResponseEntity<T> extends ResponseEntity<Map<Object, Object>> {

    public SuccessResponseEntity(HttpStatusCode status) {
        super(Map.of("success", true), status);
    }

    public SuccessResponseEntity(T body, HttpStatusCode status) {
        super(Map.of("success", true, "response", body), status);
    }
}

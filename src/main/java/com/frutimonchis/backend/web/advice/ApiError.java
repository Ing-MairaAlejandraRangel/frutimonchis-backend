package com.frutimonchis.backend.web.advice;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiError {
    private Instant timestamp = Instant.now();
    private int status;
    private String error;
    private String message;
    private String path;

    public static ApiError of(HttpStatus status, String message, String path){
        return new ApiError(Instant.now(), status.value(), status.getReasonPhrase(), message, path);
    }
}

package com.construction.tender.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ErrorResponse {
    @JsonProperty("timestamp")
    private final LocalDateTime timestamp;

    @JsonProperty("status")
    private final int status;

    @JsonProperty("errorCode")
    private final int code;

    @JsonProperty("error")
    private String error;

    @JsonProperty("description")
    private String description;

    public static ErrorResponse of(HttpStatus status, int code, String error, String description) {
        return new ErrorResponse(LocalDateTime.now(), status.value(), code, error, description);
    }
}

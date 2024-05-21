package com.swdgr6.bikeplatform.model.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class BikeApiException extends RuntimeException{
    private HttpStatus status;

    private String message;

    public BikeApiException(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }

    public BikeApiException(String message, HttpStatus status, String message1) {
        super(message);
        this.status = status;
        this.message = message1;
    }
}

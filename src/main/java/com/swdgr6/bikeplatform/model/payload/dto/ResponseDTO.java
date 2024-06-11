package com.swdgr6.bikeplatform.model.payload.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.RequestMethod;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
public class ResponseDTO {
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime timestamp;
    private int status;
    private String message;
    private Object data;
    private RequestMethod method;
    private String path;

    public ResponseDTO(LocalDateTime timestamp, int status, String message, Object data, RequestMethod method, String path) {
        this.timestamp = timestamp;
        this.status = status;
        this.message = message;
        this.data = data;
        this.method = method;
        this.path = path;
    }

    public ResponseDTO(int status, String message, Object data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }
}

package com.swdgr6.bikeplatform.model.payload.responeModel;

import com.swdgr6.bikeplatform.model.payload.dto.ResponseDTO;
import lombok.experimental.UtilityClass;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMethod;

import java.time.LocalDateTime;

@UtilityClass
public class ResponseHandler {
    public static ResponseEntity<ResponseDTO> DataResponse(Object data, String message) {
        return new ResponseEntity<>(
                ResponseDTO.builder()
                        .timestamp(LocalDateTime.now())
                        .status(HttpStatus.OK.value())
                        .message(message)
                        .data(data)
                        .build(),
                HttpStatus.OK
        );
    }

    public static ResponseEntity<ResponseDTO> ErrorResponse(HttpStatus status, Exception ex, RequestMethod method, String path) {
        String errorMessage = subString(ex.getMessage());
        return new ResponseEntity<>(
                ResponseDTO.builder()
                        .timestamp(LocalDateTime.now())
                        .status(status.value())
                        .message(errorMessage)
                        .method(method)
                        .path(path)
                        .build()
                , status
        );
    }

    private String subString(String errorMessage) {
        if (errorMessage.startsWith("java.lang.Exception:")) {
            return errorMessage.substring("java.lang.Exception:".length()).trim();
        }
        if (errorMessage.startsWith("java.lang.IllegalStateException:")) {
            return errorMessage.substring("java.lang.IllegalStateException:".length()).trim();
        }
        if (errorMessage.startsWith("java.util.NoSuchElementException:")) {
            return errorMessage.substring("java.util.NoSuchElementException:".length()).trim();
        }
        return errorMessage;
    }
}

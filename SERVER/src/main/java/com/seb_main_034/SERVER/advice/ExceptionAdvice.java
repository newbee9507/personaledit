package com.seb_main_034.SERVER.advice;

import com.seb_main_034.SERVER.exception.GlobalException;
import com.seb_main_034.SERVER.response.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ExceptionAdvice {

    @ExceptionHandler
    public ResponseEntity UserException(GlobalException e) {

        int status = e.getCode().getStatus();

        HttpStatus resultStatus = HttpStatus.valueOf(status);

        return new ResponseEntity<>(ErrorResponse.sendGlobalExResponse(e), resultStatus);
    }

    @ExceptionHandler
    public ResponseEntity UserValidException(MethodArgumentNotValidException e) {

        FieldError fieldError = e.getBindingResult().getFieldError();

        if (fieldError != null) {
            String message = fieldError.getDefaultMessage();
            return ResponseEntity.badRequest().body(message);
        }

        return ResponseEntity.badRequest().body(fieldError);
    }

}

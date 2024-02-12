package com.seb_main_034.SERVER.response;

import com.seb_main_034.SERVER.exception.ExceptionCode;
import com.seb_main_034.SERVER.exception.GlobalException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponse {

    private int status;
    private String message;

    public static ErrorResponse sendGlobalExResponse(GlobalException globalException) {
        int status = globalException.getCode().getStatus();
        String message = globalException.getCode().getMessage();

        return new ErrorResponse(status, message);
    }

    public static ErrorResponse sendErrorResponse(ExceptionCode code) {
        int status = code.getStatus();
        String message = code.getMessage();

        return new ErrorResponse(status, message);
    }

}

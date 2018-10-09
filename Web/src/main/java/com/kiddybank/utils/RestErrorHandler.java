package com.kiddybank.utils;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;

@ControllerAdvice
public class RestErrorHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = IllegalArgumentException.class)
    public final ResponseEntity<ErrorDetails> handleIllegalArgumentException (IllegalArgumentException e, WebRequest request) {
        ErrorDetails details = new ErrorDetails(new Date(), e.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(details, HttpStatus.BAD_REQUEST);
    }

    //globale error handler
    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<ErrorDetails> handleExpection(Exception e, WebRequest request) {
        ErrorDetails details = new ErrorDetails(new Date(), "Unknown error, please contact support.", request.getDescription(false));
        return new ResponseEntity<>(details, HttpStatus.INTERNAL_SERVER_ERROR);
    }


}

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

    /**
     * Illegalargumentexception opvanger, deze wordt gebruikt als de client verkeerde parameters meestuurt of bijvoorbeeld een onbestaande accountID meestuurt.
     * @param e the exception that was thrown by the request
     * @param request the webrequest containing information like the url
     * @return a responseentity of type errordetails with HTTP status 400
     */
    @ExceptionHandler(value = IllegalArgumentException.class)
    public final ResponseEntity<ErrorDetails> handleIllegalArgumentException (IllegalArgumentException e, WebRequest request) {
        ErrorDetails details = new ErrorDetails(new Date(), e.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(details, HttpStatus.BAD_REQUEST);
    }

    /**
     * Exception opvanger, deze vangt alle overige excepties die niet specifiek afgehandeld worden.
     * @param e the exception that was thrown by the request
     * @param request the webrequest containing information like the url
     * @return a responsentity of type errordetails with HTTP status 500
     */
    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<ErrorDetails> handleExpection(Exception e, WebRequest request) {
        ErrorDetails details = new ErrorDetails(new Date(), "Unknown error, please contact support.", request.getDescription(false));
        return new ResponseEntity<>(details, HttpStatus.INTERNAL_SERVER_ERROR);
    }


}

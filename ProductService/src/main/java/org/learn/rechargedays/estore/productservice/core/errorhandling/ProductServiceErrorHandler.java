package org.learn.rechargedays.estore.productservice.core.errorhandling;

import org.axonframework.commandhandling.CommandExecutionException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Date;

@ControllerAdvice
public class ProductServiceErrorHandler {

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ErrorMessage> handleIllegalStateException(IllegalStateException ex) {
        ErrorMessage errorMessage = new ErrorMessage(new Date(), ex.getMessage());
        return new ResponseEntity<>(errorMessage, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorMessage> handleException(Exception ex) {
        ErrorMessage errorMessage = new ErrorMessage(new Date(), ex.getMessage());
        return new ResponseEntity<>(errorMessage, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(CommandExecutionException.class)
    public ResponseEntity<ErrorMessage> handleCommandExecutionException(CommandExecutionException ex) {
        ErrorMessage errorMessage = new ErrorMessage(new Date(), ex.getMessage());
        return new ResponseEntity<>(errorMessage, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

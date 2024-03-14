package com.feelsafe.feelsafe.advice;

import com.feelsafe.feelsafe.exception.ResourceNotFoundException;
import com.feelsafe.feelsafe.exception.ResponseEntityBuilder;
import com.feelsafe.feelsafe.exception.TokenCustomException;
import com.feelsafe.feelsafe.model.payload.response.ApiErrorResponse;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class GlobalExceptionAdvice extends ResponseEntityExceptionHandler {


    // handleConstraintViolationException : triggers when @Validated fails
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<?> handleConstraintViolationException(Exception ex, WebRequest request) {

        List<String> details = new ArrayList<String>();
        details.add(ex.getMessage());

        ApiErrorResponse err = new ApiErrorResponse(LocalDateTime.now(),HttpStatus.BAD_REQUEST, "Constraint Violation" ,details);

        return ResponseEntityBuilder.build(err);
    }

    // handleResourceNotFoundException : triggers when there is not resource with the specified ID in BDD
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Object> handleResourceNotFoundException(ResourceNotFoundException ex) {

        List<String> details = new ArrayList<String>();
        details.add(ex.getMessage());

        ApiErrorResponse err = new ApiErrorResponse(LocalDateTime.now(),HttpStatus.NOT_FOUND, "Resource Not Found" ,details);

        return ResponseEntityBuilder.build(err);
    }
    // TokenCustomException
    @ExceptionHandler(TokenCustomException.class)
    public ResponseEntity<Object> handleTokenCustomException(TokenCustomException ex) {
        List<String> details = new ArrayList<String>();
        details.add(ex.getMessage());

        ApiErrorResponse err = new ApiErrorResponse(LocalDateTime.now(),HttpStatus.FORBIDDEN, "Custom token exception" ,details);

        return ResponseEntityBuilder.build(err);
    }


    @ExceptionHandler({ Exception.class })
    public ResponseEntity<Object> handleAll(Exception ex, WebRequest request) {

        List<String> details = new ArrayList<String>();
        details.add(ex.getLocalizedMessage());

        ApiErrorResponse err = new ApiErrorResponse(LocalDateTime.now(),HttpStatus.BAD_REQUEST, "Error occurred" ,details);

        return ResponseEntityBuilder.build(err);

    }


}

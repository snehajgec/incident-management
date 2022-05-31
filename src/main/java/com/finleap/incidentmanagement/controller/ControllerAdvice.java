package com.finleap.incidentmanagement.controller;

import com.finleap.incidentmanagement.dto.ErrorResponse;
import com.finleap.incidentmanagement.exception.IncidentManagementInternalServerException;
import com.finleap.incidentmanagement.exception.IncidentManagementNotFoundException;
import com.finleap.incidentmanagement.exception.IncidentManagementUnauthorizedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class ControllerAdvice extends ResponseEntityExceptionHandler {
        @ExceptionHandler({IncidentManagementUnauthorizedException.class})
    public ResponseEntity<ErrorResponse> handleUnauthorizedException(RuntimeException ex) {
        ErrorResponse errorResponse = ErrorResponse.builder().errorStatusCode(HttpStatus.UNAUTHORIZED.value()).errorMessage(ex.getMessage()).build();
        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler({IncidentManagementNotFoundException.class})
    public ResponseEntity<ErrorResponse> handleNotFoundException(RuntimeException ex) {
        ErrorResponse errorResponse = ErrorResponse.builder().errorStatusCode(HttpStatus.NOT_FOUND.value()).errorMessage(ex.getMessage()).build();
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({IncidentManagementInternalServerException.class})
    public ResponseEntity<ErrorResponse> handleAllOtherException(Exception ex) {
        ErrorResponse errorResponse = ErrorResponse.builder().errorStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value()).errorMessage(ex.getMessage()).build();
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

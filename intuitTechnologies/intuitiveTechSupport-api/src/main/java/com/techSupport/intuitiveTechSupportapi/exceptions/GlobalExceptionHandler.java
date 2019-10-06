package com.techSupport.intuitiveTechSupportapi.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.DefaultHandlerExceptionResolver;

@ControllerAdvice
public class GlobalExceptionHandler extends DefaultHandlerExceptionResolver {

    @ExceptionHandler(BookSlotException.class)
    public ResponseEntity<ExceptionEntity> getBookSlotErrorResponse(BookSlotException e) {
        return getResponseEntityForException(e.getClass().getName(), e.getMessage(), HttpStatus.NOT_ACCEPTABLE);
    }

    @ExceptionHandler(CancelSlotException.class)
    public ResponseEntity<ExceptionEntity> getCancelSlotErrorResponse(CancelSlotException e) {
        return getResponseEntityForException(e.getClass().getName(),e.getMessage(), HttpStatus.NOT_ACCEPTABLE);
    }

    @ExceptionHandler(EmailNotificationException.class)
    public ResponseEntity<ExceptionEntity> getEmailNotSentErrorResponse(EmailNotificationException e) {
        return getResponseEntityForException(e.getClass().getName(),e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ExceptionEntity> getEntityNotFoundErrorResponse(EntityNotFoundException e) {
        return getResponseEntityForException(e.getClass().getSimpleName(),e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(EntitySaveException.class)
    public ResponseEntity<ExceptionEntity> getEntitySaveErrorResponse(EntitySaveException e) {
        return getResponseEntityForException(e.getClass().getName(),e.getMessage(), HttpStatus.NOT_MODIFIED);
    }

    @ExceptionHandler(DataGenerationException.class)
    public ResponseEntity<ExceptionEntity> getEntitySaveErrorResponse(DataGenerationException e) {
        return getResponseEntityForException(e.getClass().getName(),e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public ResponseEntity<ExceptionEntity> getResponseEntityForException(String name,String message,HttpStatus httpStatus) {
        return new ResponseEntity<>(new ExceptionEntity(name,message), httpStatus);
    }
}

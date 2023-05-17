package ru.clevertec.ecl.spring.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ControllerErrorHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<AppError> resourceNotFoundException(NotFoundException e) {
        log.error(e.getMessage(), e);

        return new ResponseEntity<>(
                new AppError(HttpStatus.NOT_FOUND.value(), e.getMessage()),
                             HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<AppError> globalExceptionHandler(Exception ex) {

        return new ResponseEntity<>(
                new AppError(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage()),
                             HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

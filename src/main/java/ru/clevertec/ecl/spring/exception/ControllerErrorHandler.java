package ru.clevertec.ecl.spring.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ControllerErrorHandler {

    @ResponseBody
    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public AppError resourceNotFoundException(NotFoundException e) {
        log.error(e.getMessage(), e);

        return new AppError(HttpStatus.NOT_FOUND.value(), e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public AppError globalExceptionHandler(Exception ex) {

        return new AppError(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                ex.getMessage());
    }
}

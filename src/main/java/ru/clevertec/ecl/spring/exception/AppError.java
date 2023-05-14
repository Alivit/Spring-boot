package ru.clevertec.ecl.spring.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

/**
 * Класс хранящий информацию о ошибках приложения
 */
@Data
@AllArgsConstructor
public class AppError {

    /**
     * Поле со статусом ошибки
     */
    private final int status;

    /**
     * Поле с сообщением ошибки
     */
    private final String message;

    /**
     * Поле со временем ошибки
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private final LocalDateTime time = LocalDateTime.now();

}

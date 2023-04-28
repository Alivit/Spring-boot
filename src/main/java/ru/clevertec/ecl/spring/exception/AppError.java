package ru.clevertec.ecl.spring.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Класс хранящий информацию о ошибках приложения
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppError {
    /**
     * Поле со статусом ошибки
     */
    private int statusCode;
    /**
     * Поле с сообщением ошибки
     */
    private String message;
}

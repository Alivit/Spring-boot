package ru.clevertec.ecl.spring.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppError {
    private int statusCode;
    private String message;
}

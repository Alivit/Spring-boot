package ru.clevertec.ecl.spring.exception;

public class ServerErrorException extends RuntimeException{

    public ServerErrorException(String msg) {
        super(msg);
    }
}

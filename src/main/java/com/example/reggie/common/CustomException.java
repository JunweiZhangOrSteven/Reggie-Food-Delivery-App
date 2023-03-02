package com.example.reggie.common;

/**
 * Customized business exception classes
 */
public class CustomException extends RuntimeException {
    public CustomException(String message){
        super(message);
    }
}

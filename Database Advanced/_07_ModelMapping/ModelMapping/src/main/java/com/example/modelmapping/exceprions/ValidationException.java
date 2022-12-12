package com.example.modelmapping.exceprions;

public class ValidationException extends RuntimeException{
    public ValidationException(String reason){
        super(reason);
    }
}

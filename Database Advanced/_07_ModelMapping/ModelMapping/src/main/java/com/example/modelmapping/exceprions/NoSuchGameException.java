package com.example.modelmapping.exceprions;

public class NoSuchGameException extends RuntimeException{
    public NoSuchGameException() {
        super("There is no such game in the game collection.");
    }
}

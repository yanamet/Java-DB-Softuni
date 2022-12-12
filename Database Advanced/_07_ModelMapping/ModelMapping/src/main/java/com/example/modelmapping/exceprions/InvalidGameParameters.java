package com.example.modelmapping.exceprions;

public class InvalidGameParameters extends RuntimeException {
    public InvalidGameParameters(String message) {
        super(message);
    }
}

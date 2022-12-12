package com.example.modelmapping.exceprions;

public class UserAlreadyRegistered extends RuntimeException {
    public UserAlreadyRegistered() {
        super("This user is already registered.");
    }
}

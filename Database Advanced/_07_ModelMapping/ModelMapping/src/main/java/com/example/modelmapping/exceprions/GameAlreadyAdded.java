package com.example.modelmapping.exceprions;

public class GameAlreadyAdded extends RuntimeException {
    public GameAlreadyAdded() {
        super("This game already exists.");
    }
}

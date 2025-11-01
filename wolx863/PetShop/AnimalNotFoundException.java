package org.example;
public class AnimalNotFoundException extends Exception {
    public AnimalNotFoundException(String message) {
        super(message);
    }
}
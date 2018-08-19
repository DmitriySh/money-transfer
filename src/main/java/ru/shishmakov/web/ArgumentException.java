package ru.shishmakov.web;

public class ArgumentException extends RuntimeException {

    public ArgumentException() {
    }

    public ArgumentException(String message) {
        super(message);
    }
}

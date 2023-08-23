package com.example.BMSAug23.exceptions;

public class SeatsAlreadyBookedException extends Exception{
    public SeatsAlreadyBookedException() {
    }

    public SeatsAlreadyBookedException(String message) {
        super(message);
    }
}

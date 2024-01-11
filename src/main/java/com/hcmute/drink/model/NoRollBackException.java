package com.hcmute.drink.model;

public class NoRollBackException extends RuntimeException{
    public NoRollBackException(String message) {
        super(message);
    }
}

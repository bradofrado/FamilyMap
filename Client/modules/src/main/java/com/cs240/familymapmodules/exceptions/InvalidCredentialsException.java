package com.cs240.familymapmodules.exceptions;

public class InvalidCredentialsException extends Exception {
    @Override
    public String getMessage() {
        return "Invalid credentials";
    }
}

package ru.practicum.shareit.exception;

public class ConflictException extends RuntimeException {

    public ConflictException(String massage) {
        super(massage);
    }

    public String  getMassage() {
        return super.getMessage();
    }
}
package ru.practicum.shareit.common.exception;

public class ValidationException extends RuntimeException {

    public ValidationException(String massage) {
        super(massage);
    }

    public String getMassage() {
        return super.getMessage();
    }
}

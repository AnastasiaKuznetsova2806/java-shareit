package ru.practicum.shareit.exception;

public class DataNotFoundException extends RuntimeException{

    public DataNotFoundException(String massage) {
        super(massage);
    }

    public String getMassage() {
        return super.getMessage();
    }
}

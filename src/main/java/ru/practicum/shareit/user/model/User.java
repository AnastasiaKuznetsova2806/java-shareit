package ru.practicum.shareit.user.model;

import lombok.Data;

import javax.validation.constraints.Email;

@Data
public class User {
    private long id;
    private final String name;
    @Email(message = "Электронная почта не соответствует формату электронного адреса")
    private final String email;
}

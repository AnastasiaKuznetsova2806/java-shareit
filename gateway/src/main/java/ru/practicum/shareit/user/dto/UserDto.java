package ru.practicum.shareit.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.Email;

@Data
@AllArgsConstructor
public class UserDto {
    private long id;
    private final String name;
    @Email(message = "Электронная почта не соответствует формату электронного адреса")
    private final String email;
}

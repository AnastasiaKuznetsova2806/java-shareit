package ru.practicum.shareit.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserDto {
    private long id;
    private final String name;
    private final String email;
}

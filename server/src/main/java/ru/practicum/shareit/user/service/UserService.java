package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

public interface UserService {
    UserDto createUser(UserDto userDto);

    UserDto updateUser(long userId, UserDto userDto);

    List<UserDto> findAllUsers();

    UserDto findUserById(long userId);

    void deleteUserById(long userId);
}

package ru.practicum.shareit.user.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/users")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public UserDto createUser(@Valid @RequestBody UserDto userDto) {
        log.info("Получен запрос на добавление пользователя: '{}' ", userDto);
        return userService.createUser(userDto);
    }

    @PatchMapping(value = "/{userId}")
    public UserDto updateUser(@PathVariable long userId,
                           @Valid @RequestBody UserDto userDto) {
        log.info("Получен запрос на обновление пользователя: '{}'", userDto);
        return userService.updateUser(userId, userDto);
    }

    @GetMapping
    public List<UserDto> findAllUsers() {
        return userService.findAllUsers();
    }

    @GetMapping(value = "/{userId}")
    public UserDto findUserById(@PathVariable long userId) {
        return userService.findUserById(userId);
    }

    @DeleteMapping(value = "/{userId}")
    public void deleteUserById(@PathVariable long userId) {
        log.info("Получен запрос на удаление пользователя '{}'", userId);
        userService.deleteUserById(userId);
    }
}

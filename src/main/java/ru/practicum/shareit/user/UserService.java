package ru.practicum.shareit.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.util.mapper.Mapper;
import ru.practicum.shareit.util.validation.CheckDataValidation;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserStorage userStorage;
    private final CheckDataValidation validation;

    @Autowired
    public UserService(UserStorage userStorage,
                       CheckDataValidation validation) {
        this.userStorage = userStorage;
        this.validation = validation;
    }

    public UserDto createUser(UserDto userDto) {
        validation.userCheck(userDto);
        validation.userValidation(userDto);

        User user = Mapper.toUser(userDto);
        return Mapper.toUserDto(userStorage.createUser(user));
    }

    public UserDto updateUser(long userId, UserDto userDto) {
        validation.userCheck(userDto);

        User user = Mapper.toUser(userDto);
        user.setId(userId);
        return Mapper.toUserDto(userStorage.updateUser(user));
    }

    public List<UserDto> findAllUsers() {
        return userStorage.findAllUsers().stream()
                .map(Mapper::toUserDto)
                .collect(Collectors.toList());
    }

    public UserDto findUserById(long userId) {
        return Mapper.toUserDto(userStorage.findUserById(userId));
    }

    public void deleteUserById(long userId) {
        userStorage.deleteUserById(userId);
    }
}

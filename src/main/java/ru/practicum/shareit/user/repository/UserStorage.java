package ru.practicum.shareit.user.repository;

import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserStorage {
    User createUser(User user);

    User updateUser(User user);

    List<User> findAllUsers();

    User findUserById(long userId);

    void deleteUserById(long userId);
}

package ru.practicum.shareit.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.DataNotFoundException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.util.validation.CheckDataValidation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class UserStorageImpl implements UserStorage{
    private final CheckDataValidation validation;
    private long id = 1;
    private final Map<Long, User> users = new HashMap<>();

    @Autowired
    public UserStorageImpl(CheckDataValidation validation) {
        this.validation = validation;
    }

    @Override
    public User createUser(User user) {
        validation.userCheckEmail(user, users);

        long idUser = incrementId();
        user.setId(idUser);

        users.put(idUser, user);
        return user;
    }

    @Override
    public User updateUser(User user) {
        long userId = user.getId();
        validation.userCheckEmail(user, users);
        checkData(userId);

        User userOld = users.get(userId);

        User userUpdate = new User(
                user.getName() != null ? user.getName() : userOld.getName(),
                user.getEmail() != null ? user.getEmail() : userOld.getEmail()
        );
        userUpdate.setId(userId);

        users.put(userId, userUpdate);
        return userUpdate;
    }

    @Override
    public List<User> findAllUsers() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User findUserById(long userId) {
        checkData(userId);
        return users.get(userId);
    }

    @Override
    public void deleteUserById(long userId) {
        checkData(userId);
        users.remove(userId);
    }

    private long incrementId() {
        return this.id++;
    }

    private void checkData(long userId) {
        if (!users.containsKey(userId)) {
            throw new DataNotFoundException(String.format("Пользователь %d не найден", userId));
        }
    }
}

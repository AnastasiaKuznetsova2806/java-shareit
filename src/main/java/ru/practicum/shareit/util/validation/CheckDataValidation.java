package ru.practicum.shareit.util.validation;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.ConflictException;
import ru.practicum.shareit.exception.DataNotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.Map;

@Component
public class CheckDataValidation {
    public void userCheck(UserDto userDto) {
        if (userDto == null) {
            throw new DataNotFoundException("Пользователь не заполнен");
        }
    }

    public void userValidation(UserDto userDto) {
        String name = userDto.getName();
        String email = userDto.getEmail();

        if (name == null || name.isBlank()
                || email == null || email.isBlank()) {
            throw new ValidationException("Поле не может быть пустым");
        }
    }

    public void userCheckEmail(User user, Map<Long, User> users) {
        String email = user.getEmail();

        boolean result = users.values().stream()
                .anyMatch(user1 -> user1.getEmail().equals(email));

        if (result) {
            throw new ConflictException(String.format("Пользователь c %s уже существует", email));
        }
    }

    public void itemCheck(ItemDto itemDto) {
        if (itemDto == null) {
            throw new DataNotFoundException("Объект не заполнен");
        }
    }

    public void itemValidation(ItemDto itemDto) {
        String name = itemDto.getName();
        String description = itemDto.getDescription();
        Boolean available = itemDto.getAvailable();

        if (name == null || name.isBlank()
                || description == null || description.isBlank()
                || available == null
        ) {
            throw new ValidationException("Поле не может быть пустым");
        }
    }

    public void itemCheckUser(long userId, Item item) {
        if (userId != item.getOwner().getId()) {
            throw new ValidationException("Редактировать вещь может только её владелец");
        }
    }
}

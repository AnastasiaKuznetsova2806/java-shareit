package ru.practicum.shareit.common.validation;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.common.exception.DataNotFoundException;
import ru.practicum.shareit.common.exception.ValidationException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDate;
import java.time.LocalDateTime;

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
            throw new DataNotFoundException("Редактировать вещь может только её владелец");
        }
    }

    public void bookingCheck(BookingDto bookingDto) {
        if (bookingDto == null) {
            throw new DataNotFoundException("Объект не заполнен");
        }
    }

    public void checkDataTime(LocalDateTime start, LocalDateTime end) {
        LocalDate timeNow = LocalDate.now();

        if (end.isBefore(start)) {
            throw new ValidationException("Дата и время конца бронирования не может быть раньше начала бронирования");
        }
        LocalDate startDate = start.toLocalDate();
        if (startDate.isBefore(timeNow)) {
            throw new ValidationException("Дата и время начала бронирования не может быть в прошлом");
        }
    }
}

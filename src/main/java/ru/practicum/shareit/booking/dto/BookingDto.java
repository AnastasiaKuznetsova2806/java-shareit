package ru.practicum.shareit.booking.dto;

import lombok.Data;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.util.BookingStatus;

import java.time.LocalDateTime;

@Data
public class BookingDto {
    private long id;
    private final LocalDateTime start;
    private final LocalDateTime end;
    private final Item item;
    private final User booker;
    private final BookingStatus status;
}

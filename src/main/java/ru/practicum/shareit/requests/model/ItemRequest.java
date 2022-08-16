package ru.practicum.shareit.requests.model;

import lombok.Data;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

@Data
public class ItemRequest {
    private long id;
    private final String description;
    private final User requestor;
    private final LocalDateTime created;
}

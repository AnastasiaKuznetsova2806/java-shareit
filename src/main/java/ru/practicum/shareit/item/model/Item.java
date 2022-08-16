package ru.practicum.shareit.item.model;

import lombok.Data;
import ru.practicum.shareit.requests.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

@Data
public class Item {
    private long id;
    private final String name;
    private final String description;
    private final Boolean available;
    private User owner;
    private ItemRequest request;

    public Item(long id, String name, String description, Boolean available) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.available = available;
    }
}

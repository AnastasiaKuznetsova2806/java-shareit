package ru.practicum.shareit.common.mapper;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

public class Mapper {

    private Mapper() {
    }

    public static ItemDto toItemDto(Item item) {
        return new ItemDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable()
        );
    }

    public static Item toItem(ItemDto itemDto) {
        return new Item(
                itemDto.getId(),
                itemDto.getName() != null ? itemDto.getName() : null,
                itemDto.getDescription() != null ? itemDto.getDescription() : null,
                itemDto.getAvailable() != null ? itemDto.getAvailable() : null
        );
    }

    public static UserDto toUserDto(User user) {
        return new UserDto(
                user.getId(),
                user.getName(),
                user.getEmail()
        );
    }

    public static User toUser(UserDto userDto) {
        return new User(
                userDto.getName(),
                userDto.getEmail()
        );
    }
}

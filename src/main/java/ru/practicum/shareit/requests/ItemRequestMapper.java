package ru.practicum.shareit.requests;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.requests.dto.ItemRequestDto;
import ru.practicum.shareit.requests.dto.ItemRequestInfoDto;
import ru.practicum.shareit.requests.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.Set;

@UtilityClass
public class ItemRequestMapper {
    public static ItemRequestDto toItemRequestDto(ItemRequest itemRequest) {
        return new ItemRequestDto(
                itemRequest.getId(),
                itemRequest.getDescription(),
                itemRequest.getCreated()
        );
    }

    public static ItemRequest toItemRequest(ItemRequestDto itemRequestDto, User user) {
        return new ItemRequest(
                itemRequestDto.getId(),
                itemRequestDto.getDescription(),
                user,
                LocalDateTime.now()
        );
    }

    public static ItemRequestInfoDto toItemRequestInfoDto(ItemRequest itemRequest, Set<ItemDto> items) {
        return new ItemRequestInfoDto(
                itemRequest.getId(),
                itemRequest.getDescription(),
                itemRequest.getCreated(),
                items
        );
    }

    public static ItemRequestInfoDto toItemRequestInfoDto(ItemRequestDto itemRequestDto, Set<ItemDto> items) {
        return new ItemRequestInfoDto(
                itemRequestDto.getId(),
                itemRequestDto.getDescription(),
                itemRequestDto.getCreated(),
                items
        );
    }
}

package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemService {
    ItemDto createItem(long userId, ItemDto itemDto);

    ItemDto updateItem(long userId, long itemId, ItemDto itemDto);

    ItemDto findItemById(long userId, long itemId);

    List<ItemDto> findAllItemByUserId(long userId);

    List<ItemDto> findAllItem();

    void deleteItemById(long userId, long itemId);

    List<ItemDto> searchItem(long userId, String text);
}

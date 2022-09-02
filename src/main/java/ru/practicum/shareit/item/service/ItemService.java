package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemInfoDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {
    ItemDto createItem(long userId, ItemDto itemDto);

    ItemDto updateItem(long userId, long itemId, ItemDto itemDto);

    ItemInfoDto findItemById(long userId, long itemId);

    List<ItemInfoDto> findAllItemByUserId(long userId);

    List<ItemDto> findAllItem();

    void deleteItemById(long userId, long itemId);

    List<ItemDto> searchItem(long userId, String text);

    Item getItemById(long itemId);
}

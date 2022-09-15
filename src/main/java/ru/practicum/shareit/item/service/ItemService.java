package ru.practicum.shareit.item.service;

import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemInfoDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {
    ItemDto createItem(long userId, ItemDto itemDto);

    ItemDto updateItem(long userId, long itemId, ItemDto itemDto);

    ItemInfoDto findItemById(long userId, long itemId);

    List<ItemInfoDto> findAllItemByUserId(long userId, Pageable pageable);

    List<ItemDto> findAllItem(Pageable pageable);

    void deleteItemById(long userId, long itemId);

    List<ItemDto> searchItem(long userId, String text, Pageable pageable);

    Item getItemById(long itemId);
}

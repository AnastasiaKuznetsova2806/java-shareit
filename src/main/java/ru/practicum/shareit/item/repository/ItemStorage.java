package ru.practicum.shareit.item.repository;

import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemStorage {
    Item createItem(long userId, Item item);

    Item updateItem(long userId, Item item);

    Item findItemById(long itemId);

    List<Item> findAllItemByUserId(long userId);

    List<Item> findAllItem();

    void deleteItemById(long userId, long itemId);
}

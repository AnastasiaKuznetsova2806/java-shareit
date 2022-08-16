package ru.practicum.shareit.item;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.DataNotFoundException;
import ru.practicum.shareit.item.model.Item;

import java.util.*;

@Component
public class ItemStorageImpl implements ItemStorage{
    private long id = 1;
    private final Map<Long, Item> items = new HashMap<>();
    private final Map<Long, Map<Long, Item>> userItems = new HashMap<>();

    @Override
    public Item createItem(long userId, Item item) {
        Map<Long, Item> itemMap = new HashMap<>();
        long id = incrementId();
        item.setId(id);

        if (userItems.containsKey(userId)) {
            itemMap = userItems.get(userId);
        }
        itemMap.put(id, item);
        userItems.put(userId, itemMap);
        items.put(id, item);
        return item;
    }

    @Override
    public Item updateItem(long userId, Item item) {
        long id = item.getId();

        Map<Long, Item> itemMap = getListItemsByUserId(userId);
        itemMap.put(id, item);
        items.put(id, item);
        return item;
    }

    @Override
    public Item findItemById(long itemId) {
        return items.get(itemId);
    }

    @Override
    public List<Item> findAllItemByUserId(long userId) {
        return new ArrayList<>(getListItemsByUserId(userId).values());
    }

    @Override
    public List<Item> findAllItem() {
        return new ArrayList<>(items.values());
    }

    @Override
    public void deleteItemById(long userId, long itemId) {
        getListItemsByUserId(userId).remove(itemId);
    }

    private long incrementId() {
        return this.id++;
    }

    private Map<Long, Item> getListItemsByUserId(long userId) {
        if (!userItems.containsKey(userId)) {
            throw new DataNotFoundException(String.format("Пользователь %d не найден", userId));
        }
        return userItems.get(userId);
    }
}

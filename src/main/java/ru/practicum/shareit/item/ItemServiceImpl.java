package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserStorage;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.util.mapper.Mapper;
import ru.practicum.shareit.util.validation.CheckDataValidation;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ItemServiceImpl implements ItemService{
    private final CheckDataValidation validation;
    private final ItemStorage itemStorage;
    private final UserStorage userStorage;

    @Autowired
    public ItemServiceImpl(CheckDataValidation validation,
                           ItemStorage itemStorage,
                           UserStorage userStorage) {
        this.validation = validation;
        this.itemStorage = itemStorage;
        this.userStorage = userStorage;
    }

    @Override
    public ItemDto createItem(long userId, ItemDto itemDto) {
        validation.itemCheck(itemDto);
        validation.itemValidation(itemDto);
        Item item = checkUserIdAndGetItem(userId, itemDto);

        return Mapper.toItemDto(itemStorage.createItem(userId, item));
    }

    @Override
    public ItemDto updateItem(long userId, long itemId, ItemDto itemDto) {
        validation.itemCheck(itemDto);
        Item itemUpdate = checkUserIdAndGetItem(userId, itemDto);
        validation.itemCheckUser(userId, itemUpdate);

        Item itemOld = itemStorage.findItemById(itemId);

        Item item = new Item(
                itemId,
                itemUpdate.getName() != null ? itemUpdate.getName() : itemOld.getName(),
                itemUpdate.getDescription() != null ? itemUpdate.getDescription() : itemOld.getDescription(),
                itemUpdate.getAvailable() != null ? itemUpdate.getAvailable() : itemOld.getAvailable()
        );
        return Mapper.toItemDto(itemStorage.updateItem(userId, item));
    }

    @Override
    public ItemDto findItemById(long userId, long itemId) {
        return Mapper.toItemDto(itemStorage.findItemById(itemId));
    }

    @Override
    public List<ItemDto> findAllItemByUserId(long userId) {
        userStorage.findUserById(userId);

        return itemStorage.findAllItemByUserId(userId).stream()
                .map(Mapper::toItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemDto> findAllItem() {
        return itemStorage.findAllItem().stream()
                .map(Mapper::toItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemDto> searchItem(long userId, String text) {
        if (!text.isEmpty() || !text.isBlank()) {
            return findAllItem().stream()
                    .filter(itemDto -> (
                            itemDto.getName().toLowerCase().contains(text.toLowerCase()) ||
                                    itemDto.getDescription().toLowerCase().contains(text.toLowerCase()
                                    )
                    ))
                    .filter(ItemDto::getAvailable)
                    .collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    @Override
    public void deleteItemById(long userId, long itemId) {
        itemStorage.deleteItemById(userId, itemId);
    }

    private Item checkUserIdAndGetItem(long userId, ItemDto itemDto) {
        User user = userStorage.findUserById(userId);
        Item item = Mapper.toItem(itemDto);
        item.setOwner(user);
        return item;
    }
}

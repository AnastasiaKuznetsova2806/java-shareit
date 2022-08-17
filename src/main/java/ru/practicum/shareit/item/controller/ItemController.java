package ru.practicum.shareit.item.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/items")
public class ItemController {
    private final ItemService itemService;

    @Autowired
    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @PostMapping
    public ItemDto createItem(@RequestHeader("X-Sharer-User-Id") long userId,
                              @Valid @RequestBody ItemDto itemDto) {
        log.info("Получен запрос на добавление объекта: '{}' ", itemDto);
        return itemService.createItem(userId, itemDto);
    }

    @PatchMapping(value = "/{itemId}")
    public ItemDto updateItem(@RequestHeader("X-Sharer-User-Id") long userId,
                              @PathVariable long itemId,
                              @Valid @RequestBody ItemDto itemDto) {
        log.info("Получен запрос на обновление объекта: '{}' ", itemDto);
        return itemService.updateItem(userId, itemId, itemDto);
    }

    @GetMapping(value = "/{itemId}")
    public ItemDto findItemById(@RequestHeader("X-Sharer-User-Id") long userId,
                                @PathVariable long itemId) {
        return itemService.findItemById(userId, itemId);
    }

    @GetMapping
    public List<ItemDto> findAllItemByUserId(@RequestHeader("X-Sharer-User-Id") long userId) {
        return itemService.findAllItemByUserId(userId);
    }

    @GetMapping(value = "/search")
    public List<ItemDto> searchItem(@RequestHeader("X-Sharer-User-Id") long userId,
                                    @RequestParam(value = "text", defaultValue = "") String text) {
        return itemService.searchItem(userId, text);
    }

    @DeleteMapping(value = "/{itemId}")
    public void deleteItemById(@RequestHeader("X-Sharer-User-Id") long userId,
                               @PathVariable long itemId) {
        log.info("Получен запрос на удаление объекта '{}'", userId);
        itemService.deleteItemById(itemId, userId);
    }
}

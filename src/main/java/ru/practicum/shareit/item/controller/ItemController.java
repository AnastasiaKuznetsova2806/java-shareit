package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.comment.dto.CommentDto;
import ru.practicum.shareit.item.comment.dto.CommentInfoDto;
import ru.practicum.shareit.item.comment.servise.CommentService;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemInfoDto;
import ru.practicum.shareit.item.service.ItemService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {
    private static final String USER_ID_HEADER = "X-Sharer-User-Id";
    private final ItemService itemService;
    private final CommentService commentService;

    @PostMapping
    public ItemDto createItem(@RequestHeader(USER_ID_HEADER) long userId,
                              @Valid @RequestBody ItemDto itemDto) {
        log.info("Получен запрос на добавление объекта: '{}' ", itemDto);
        return itemService.createItem(userId, itemDto);
    }

    @PatchMapping(value = "/{itemId}")
    public ItemDto updateItem(@RequestHeader(USER_ID_HEADER) long userId,
                              @PathVariable long itemId,
                              @Valid @RequestBody ItemDto itemDto) {
        log.info("Получен запрос на обновление объекта: '{}' ", itemDto);
        return itemService.updateItem(userId, itemId, itemDto);
    }

    @GetMapping(value = "/{itemId}")
    public ItemInfoDto findItemById(@RequestHeader(USER_ID_HEADER) long userId,
                                    @PathVariable long itemId) {
        return itemService.findItemById(userId, itemId);
    }

    @GetMapping
    public List<ItemInfoDto> findAllItemByUserId(@RequestHeader(USER_ID_HEADER) long userId) {
        return itemService.findAllItemByUserId(userId);
    }

    @GetMapping(value = "/search")
    public List<ItemDto> searchItem(@RequestHeader(USER_ID_HEADER) long userId,
                                    @RequestParam(value = "text", defaultValue = "") String text) {
        return itemService.searchItem(userId, text);
    }

    @DeleteMapping(value = "/{itemId}")
    public void deleteItemById(@RequestHeader(USER_ID_HEADER) long userId,
                               @PathVariable long itemId) {
        log.info("Получен запрос на удаление объекта '{}'", userId);
        itemService.deleteItemById(itemId, userId);
    }

    @PostMapping(value = "/{itemId}/comment")
    public CommentInfoDto createComment(@RequestHeader(USER_ID_HEADER) long userId,
                                        @PathVariable long itemId,
                                        @Valid @RequestBody CommentDto comment) {
        log.info("Получен запрос на добавление комментария: '{}' ", comment);
        return commentService.createComment(userId, itemId, comment);
    }
}

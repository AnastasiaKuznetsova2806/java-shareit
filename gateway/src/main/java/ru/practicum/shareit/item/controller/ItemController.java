package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.common.CommonConstant;
import ru.practicum.shareit.item.comment.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {
    private final ItemClient itemClient;

    @PostMapping
    public ResponseEntity<Object> createItem(@RequestHeader(CommonConstant.USER_ID_HEADER) long userId,
                                             @Valid @RequestBody ItemDto itemDto) {
        log.info("Получен запрос на добавление объекта: '{}' ", itemDto);
        return itemClient.createItem(userId, itemDto);
    }

    @PatchMapping(value = "/{itemId}")
    public ResponseEntity<Object> updateItem(@RequestHeader(CommonConstant.USER_ID_HEADER) long userId,
                              @PathVariable long itemId,
                              @Valid @RequestBody ItemDto itemDto) {
        log.info("Получен запрос на обновление объекта: itemId={} ", itemDto);
        return itemClient.updateItem(userId, itemId, itemDto);
    }

    @GetMapping(value = "/{itemId}")
    public ResponseEntity<Object> findItemById(@RequestHeader(CommonConstant.USER_ID_HEADER) long userId,
                                    @PathVariable long itemId) {
        log.info("Получен запрос на получение объекта по itemId={}, userId={}",itemId, userId);
        return itemClient.findItemById(userId, itemId);
    }

    @GetMapping
    public ResponseEntity<Object> findAllItemByUserId(
            @RequestHeader(CommonConstant.USER_ID_HEADER) long userId,
            @PositiveOrZero @RequestParam(defaultValue = CommonConstant.DEFAULT_FROM) int from,
            @Positive @RequestParam(defaultValue = CommonConstant.DEFAULT_SIZE) int size) {
        log.info("Получен запрос на получение объекта по userId={}, from={}, size={}", userId, from, size);
        return itemClient.findAllItemByUserId(userId, from, size);
    }

    @GetMapping(value = "/search")
    public ResponseEntity<Object> searchItem(
            @RequestHeader(CommonConstant.USER_ID_HEADER) long userId,
            @RequestParam(defaultValue = "") String text,
            @PositiveOrZero @RequestParam(defaultValue = CommonConstant.DEFAULT_FROM) int from,
            @Positive @RequestParam(defaultValue = CommonConstant.DEFAULT_SIZE) int size) {
        log.info("Получен запрос на поиск объекта по search = '{}':userId={}, from={}, size={}",
                text, userId, from, size);
        return itemClient.searchItem(userId, text, from, size);
    }

    @DeleteMapping(value = "/{itemId}")
    public void deleteItemById(@RequestHeader(CommonConstant.USER_ID_HEADER) long userId,
                               @PathVariable long itemId) {
        log.info("Получен запрос на удаление объекта itemId={}, userId={}",itemId, userId);
        itemClient.deleteItemById(itemId, userId);
    }

    @PostMapping(value = "/{itemId}/comment")
    public ResponseEntity<Object> createComment(@RequestHeader(CommonConstant.USER_ID_HEADER) long userId,
                                        @PathVariable long itemId,
                                        @Valid @RequestBody CommentDto comment) {
        log.info("Получен запрос на добавление комментария для itemId={}: '{}', userId={}",itemId, comment, userId);
        return itemClient.createComment(userId, itemId, comment);
    }
}

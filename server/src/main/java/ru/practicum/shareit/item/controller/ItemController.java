package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.common.CommonConstant;
import ru.practicum.shareit.common.validation.PaginationUtil;
import ru.practicum.shareit.item.comment.dto.CommentDto;
import ru.practicum.shareit.item.comment.dto.CommentInfoDto;
import ru.practicum.shareit.item.comment.servise.CommentService;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemInfoDto;
import ru.practicum.shareit.item.service.ItemService;

import java.util.List;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {
    private final ItemService itemService;
    private final CommentService commentService;

    @PostMapping
    public ItemDto createItem(@RequestHeader(CommonConstant.USER_ID_HEADER) long userId,
                              @RequestBody ItemDto itemDto) {
        log.info("Получен запрос на добавление объекта: '{}' ", itemDto);
        return itemService.createItem(userId, itemDto);
    }

    @PatchMapping(value = "/{itemId}")
    public ItemDto updateItem(@RequestHeader(CommonConstant.USER_ID_HEADER) long userId,
                              @PathVariable long itemId,
                              @RequestBody ItemDto itemDto) {
        log.info("Получен запрос на обновление объекта: '{}' ", itemDto);
        return itemService.updateItem(userId, itemId, itemDto);
    }

    @GetMapping(value = "/{itemId}")
    public ItemInfoDto findItemById(@RequestHeader(CommonConstant.USER_ID_HEADER) long userId,
                                    @PathVariable long itemId) {
        log.info("Получен запрос на получение объекта по itemId={}, userId={}", itemId, userId);
        return itemService.findItemById(userId, itemId);
    }

    @GetMapping
    public List<ItemInfoDto> findAllItemByUserId(
            @RequestHeader(CommonConstant.USER_ID_HEADER) long userId,
            @RequestParam(defaultValue = CommonConstant.DEFAULT_FROM) int from,
            @RequestParam(defaultValue = CommonConstant.DEFAULT_SIZE) int size) {
        log.info("Получен запрос на получение объекта по userId={}, from={}, size={}", userId, from, size);
        Pageable pageable = PaginationUtil.getPageable(from, size);
        return itemService.findAllItemByUserId(userId, pageable);
    }

    @GetMapping(value = "/search")
    public List<ItemDto> searchItem(
            @RequestHeader(CommonConstant.USER_ID_HEADER) long userId,
            @RequestParam(defaultValue = "") String text,
            @RequestParam(defaultValue = CommonConstant.DEFAULT_FROM) int from,
            @RequestParam(defaultValue = CommonConstant.DEFAULT_SIZE) int size) {
        log.info("Получен запрос на поиск объекта по search = '{}':userId={}, from={}, size={}",
                text, userId, from, size);
        Pageable pageable = PaginationUtil.getPageable(from, size);
        return itemService.searchItem(userId, text, pageable);
    }

    @DeleteMapping(value = "/{itemId}")
    public void deleteItemById(@RequestHeader(CommonConstant.USER_ID_HEADER) long userId,
                               @PathVariable long itemId) {
        log.info("Получен запрос на удаление объекта '{}'", userId);
        itemService.deleteItemById(itemId, userId);
    }

    @PostMapping(value = "/{itemId}/comment")
    public CommentInfoDto createComment(@RequestHeader(CommonConstant.USER_ID_HEADER) long userId,
                                        @PathVariable long itemId,
                                        @RequestBody CommentDto comment) {
        log.info("Получен запрос на добавление комментария: '{}' ", comment);
        return commentService.createComment(userId, itemId, comment);
    }
}

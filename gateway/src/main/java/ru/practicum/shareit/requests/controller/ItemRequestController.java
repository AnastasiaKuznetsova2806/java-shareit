package ru.practicum.shareit.requests.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.common.CommonConstant;
import ru.practicum.shareit.requests.dto.ItemRequestDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/requests")
public class ItemRequestController {
    private final ItemRequestClient itemRequestClient;

    @PostMapping
    public ResponseEntity<Object> createItemRequest(@RequestHeader(CommonConstant.USER_ID_HEADER) long userId,
                                                    @Valid @RequestBody ItemRequestDto itemRequestDto) {
        log.info("Получен запрос на добавление объекта: '{}' ", itemRequestDto);
        return itemRequestClient.createItemRequest(userId, itemRequestDto);
    }

    @GetMapping
    public ResponseEntity<Object>  findAllItemRequest(@RequestHeader(CommonConstant.USER_ID_HEADER) long userId) {
        log.info("Получен запрос на получение объектов: userId={} ", userId);
        return itemRequestClient.findAllItemRequest(userId);
    }

    @GetMapping(value = "/all")
    public ResponseEntity<Object>  findAllItemRequestByOtherUser(
            @RequestHeader(CommonConstant.USER_ID_HEADER) long userId,
            @PositiveOrZero @RequestParam(defaultValue = CommonConstant.DEFAULT_FROM) int from,
            @Positive @RequestParam(defaultValue = CommonConstant.DEFAULT_SIZE) int size) {
        log.info("Получен запрос на получение объектов другого пользователя: userId={}, from={}, size={}",
                userId, from, size);
        return itemRequestClient.findAllItemRequestByOtherUser(userId, from, size);
    }

    @GetMapping(value = "{requestId}")
    public ResponseEntity<Object>  findItemRequestById(@RequestHeader(CommonConstant.USER_ID_HEADER) long userId,
                                                  @PathVariable long requestId) {
        return itemRequestClient.findItemRequestById(userId, requestId);
    }
}

package ru.practicum.shareit.requests.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.common.CommonConstant;
import ru.practicum.shareit.common.validation.PaginationUtil;
import ru.practicum.shareit.requests.dto.ItemRequestDto;
import ru.practicum.shareit.requests.dto.ItemRequestInfoDto;
import ru.practicum.shareit.requests.service.ItemRequestService;

import java.util.List;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/requests")
public class ItemRequestController {
    private final ItemRequestService itemRequestService;

    @PostMapping
    public ItemRequestDto createItemRequest(@RequestHeader(CommonConstant.USER_ID_HEADER) long userId,
                                            @RequestBody ItemRequestDto itemRequestDto) {
        log.info("Получен запрос на добавление объекта: '{}' ", itemRequestDto);
        return itemRequestService.createItemRequest(userId, itemRequestDto);
    }

    @GetMapping
    public List<ItemRequestInfoDto> findAllItemRequest(@RequestHeader(CommonConstant.USER_ID_HEADER) long userId) {
        return itemRequestService.findAllItemRequest(userId);
    }

    @GetMapping(value = "/all")
    public List<ItemRequestInfoDto> findAllItemRequestByOtherUser(
            @RequestHeader(CommonConstant.USER_ID_HEADER) long userId,
            @RequestParam(defaultValue = CommonConstant.DEFAULT_FROM) int from,
            @RequestParam(defaultValue = CommonConstant.DEFAULT_SIZE) int size) {
        Pageable pageable = PaginationUtil.getPageable(from, size);
        return itemRequestService.findAllItemRequestByOtherUser(userId, pageable);
    }

    @GetMapping(value = "{requestId}")
    public ItemRequestInfoDto findItemRequestById(@RequestHeader(CommonConstant.USER_ID_HEADER) long userId,
                                                  @PathVariable long requestId) {
        return itemRequestService.findItemRequestById(userId, requestId);
    }
}

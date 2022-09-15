package ru.practicum.shareit.requests.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.common.validation.Pagination;
import ru.practicum.shareit.requests.dto.ItemRequestDto;
import ru.practicum.shareit.requests.dto.ItemRequestInfoDto;
import ru.practicum.shareit.requests.service.ItemRequestService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/requests")
public class ItemRequestController {
    private static final String USER_ID_HEADER = "X-Sharer-User-Id";
    private static final String DEFAULT_FROM = "0";
    private static final String DEFAULT_SIZE = "10";
    private final ItemRequestService itemRequestService;

    @PostMapping
    public ItemRequestDto createItemRequest(@RequestHeader(USER_ID_HEADER) long userId,
                                            @Valid @RequestBody ItemRequestDto itemRequestDto) {
        log.info("Получен запрос на добавление объекта: '{}' ", itemRequestDto);
        return itemRequestService.createItemRequest(userId, itemRequestDto);
    }

    @GetMapping
    public List<ItemRequestInfoDto> findAllItemRequest(@RequestHeader(USER_ID_HEADER) long userId) {
        return itemRequestService.findAllItemRequest(userId);
    }

    @GetMapping(value = "/all")
    public List<ItemRequestInfoDto> findAllItemRequestByOtherUser(
            @RequestHeader(USER_ID_HEADER) long userId,
            @PositiveOrZero @RequestParam(value = "from", defaultValue = DEFAULT_FROM) int from,
            @Positive @RequestParam(value = "size", defaultValue = DEFAULT_SIZE) int size) {
        Pageable pageable = Pagination.getPageable(from, size);
        return itemRequestService.findAllItemRequestByOtherUser(userId, pageable);
    }

    @GetMapping(value = "{requestId}")
    public ItemRequestInfoDto findItemRequestById(@RequestHeader(USER_ID_HEADER) long userId,
                                                  @PathVariable long requestId) {
        return itemRequestService.findItemRequestById(userId, requestId);
    }
}

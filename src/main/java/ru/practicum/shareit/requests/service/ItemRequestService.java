package ru.practicum.shareit.requests.service;

import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.requests.dto.ItemRequestDto;
import ru.practicum.shareit.requests.dto.ItemRequestInfoDto;

import java.util.List;

public interface ItemRequestService {
    ItemRequestDto createItemRequest(long userId, ItemRequestDto itemRequestDto);

    List<ItemRequestInfoDto> findAllItemRequest(long userId);

    List<ItemRequestInfoDto> findAllItemRequestByOtherUser(long userId, Pageable pageable);

    ItemRequestInfoDto findItemRequestById(long userId, long requestId);
}

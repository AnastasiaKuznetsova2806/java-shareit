package ru.practicum.shareit.requests.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.common.exception.DataNotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.requests.ItemRequestMapper;
import ru.practicum.shareit.requests.dto.ItemRequestDto;
import ru.practicum.shareit.requests.dto.ItemRequestInfoDto;
import ru.practicum.shareit.requests.model.ItemRequest;
import ru.practicum.shareit.requests.repository.ItemRequestRepository;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemRequestServiceImpl implements ItemRequestService {
    private final ItemRequestRepository itemRequestRepository;
    private final UserService userService;

    @Override
    public ItemRequestDto createItemRequest(long userId, ItemRequestDto itemRequestDto) {
        User user = UserMapper.toUser(userService.findUserById(userId));
        ItemRequest itemRequest = ItemRequestMapper.toItemRequest(itemRequestDto, user);

        return ItemRequestMapper.toItemRequestDto(itemRequestRepository.save(itemRequest));
    }

    @Override
    public List<ItemRequestInfoDto> findAllItemRequest(long userId) {
        userService.findUserById(userId);
        List<ItemRequest> requests = itemRequestRepository.findItemRequestByRequestor_Id(userId);

        return requests.stream()
                .map(itemRequest -> ItemRequestMapper.toItemRequestInfoDto(
                        itemRequest,
                        itemRequestRepository.findItemsByRequestId(itemRequest.getId())
                ))
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemRequestInfoDto> findAllItemRequestByOtherUser(long userId, Pageable pageable) {
        userService.findUserById(userId);

        Page<ItemRequestDto> requests = itemRequestRepository.findAllItemRequestByOtherUser(userId, pageable);
        return requests.stream()
                .map(itemRequestDto -> ItemRequestMapper.toItemRequestInfoDto(
                        itemRequestDto,
                        itemRequestRepository.findItemsByRequestId(itemRequestDto.getId())
                ))
                .collect(Collectors.toList());
    }

    @Override
    public ItemRequestInfoDto findItemRequestById(long userId, long requestId) {
        userService.findUserById(userId);
        ItemRequest request = itemRequestRepository.findById(requestId)
                .orElseThrow(
                        () -> new DataNotFoundException(String.format("Запрос %d не найден", requestId))
                );

        Set<ItemDto> items = itemRequestRepository.findItemsByRequestId(request.getId());

        return ItemRequestMapper.toItemRequestInfoDto(request, items);
    }
}

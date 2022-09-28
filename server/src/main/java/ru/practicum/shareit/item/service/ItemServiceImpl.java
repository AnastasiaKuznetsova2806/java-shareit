package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.common.exception.DataNotFoundException;
import ru.practicum.shareit.common.validation.CheckDataValidation;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.comment.dto.CommentInfoDto;
import ru.practicum.shareit.item.comment.servise.CommentService;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemInfoDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private static final int LIMIT = 2;
    private final CheckDataValidation validation;
    private final ItemRepository itemRepository;
    private final UserService userService;
    private final CommentService commentService;

    @Override
    public ItemDto createItem(long userId, ItemDto itemDto) {
        validation.itemCheck(itemDto);
        validation.itemValidation(itemDto);
        Item item = checkUserIdAndGetItem(userId, itemDto);

        return ItemMapper.toItemDto(itemRepository.save(item));
    }

    @Override
    public ItemDto updateItem(long userId, long itemId, ItemDto itemDto) {
        validation.itemCheck(itemDto);
        Item itemUpdate = checkUserIdAndGetItem(userId, itemDto);
        Item itemOld = getItemById(itemId);
        validation.itemCheckUser(userId, itemOld);

        Item item = new Item(
                itemId,
                itemUpdate.getName() != null ? itemUpdate.getName() : itemOld.getName(),
                itemUpdate.getDescription() != null ? itemUpdate.getDescription() : itemOld.getDescription(),
                itemUpdate.getAvailable() != null ? itemUpdate.getAvailable() : itemOld.getAvailable(),
                itemUpdate.getOwner() != null ? itemUpdate.getOwner() : itemOld.getOwner(),
                itemUpdate.getRequestId() != null ? itemUpdate.getRequestId() : null
        );
        return ItemMapper.toItemDto(itemRepository.save(item));
    }

    @Override
    public ItemInfoDto findItemById(long userId, long itemId) {
        List<BookingDto> bookings = itemRepository.findBookingForItem(itemId, userId)
                .stream()
                .limit(LIMIT)
                .sorted()
                .collect(Collectors.toList());
        BookingDto lastBooking = bookings.size() > 0 ? bookings.get(0) : null;
        BookingDto nextBooking = bookings.size() >= LIMIT ? bookings.get(1) : null;

        Set<CommentInfoDto> comments = commentService.findAllCommentsForItem(itemId);
        Item item = getItemById(itemId);
        return ItemMapper.toItemInfoDto(item, lastBooking, nextBooking, comments);
    }

    @Override
    public List<ItemInfoDto> findAllItemByUserId(long userId, Pageable pageable) {
        return itemRepository.findAllItemByOwner_Id(userId, pageable).stream()
                .map(item -> findItemById(userId, item.getId()))
                .sorted()
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemDto> findAllItem(Pageable pageable) {
        return itemRepository.findAll(pageable).stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemDto> searchItem(long userId, String text, Pageable pageable) {
        if (!text.isEmpty() || !text.isBlank()) {
            return findAllItem(pageable).stream()
                    .filter(itemDto -> searchInNameOrDescription(itemDto, text))
                    .filter(ItemDto::getAvailable)
                    .collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    @Override
    public void deleteItemById(long userId, long itemId) {
        itemRepository.deleteById(itemId);
    }

    @Override
    public Item getItemById(long itemId) {
        return itemRepository.findById(itemId)
                .orElseThrow(
                        () -> new DataNotFoundException(String.format("Объект %d не найден", itemId))
                );
    }

    private Item checkUserIdAndGetItem(long userId, ItemDto itemDto) {
        User user = UserMapper.toUser(userService.findUserById(userId));
        return ItemMapper.toItem(itemDto, user);
    }

    private boolean searchInNameOrDescription(ItemDto itemDto, String text) {
        return itemDto.getName().toLowerCase().contains(text.toLowerCase()) ||
                itemDto.getDescription().toLowerCase().contains(text.toLowerCase());
    }
}

package ru.practicum.shareit.item;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.item.comment.dto.CommentInfoDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemInfoDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.HashSet;
import java.util.Set;

@UtilityClass
public class ItemMapper {
    public static ItemDto toItemDto(Item item) {
        return new ItemDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                item.getOwner().getId()
        );
    }

    public static ItemInfoDto itemInfoDto(Item item,
                                          BookingDto lastBooking,
                                          BookingDto nextBooking,
                                          Set<CommentInfoDto> comments) {
        return new ItemInfoDto(
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                lastBooking != null ? new ItemInfoDto.BookingForItem(
                        lastBooking.getId(),
                        lastBooking.getBooker(),
                        lastBooking.getStart(),
                        lastBooking.getEnd()
                ) : null,
                nextBooking != null ? new ItemInfoDto.BookingForItem(
                        nextBooking.getId(),
                        nextBooking.getBooker(),
                        nextBooking.getStart(),
                        nextBooking.getEnd()
                ) : null,
                comments != null ? comments : new HashSet<>(),
                item.getId()
        );
    }

    public static Item toItem(ItemDto itemDto, User user) {
        return new Item(
                itemDto.getId(),
                itemDto.getName() != null ? itemDto.getName() : null,
                itemDto.getDescription() != null ? itemDto.getDescription() : null,
                itemDto.getAvailable() != null ? itemDto.getAvailable() : null,
                user
        );
    }
}

package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.BookingMapper;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingInfoDto;
import ru.practicum.shareit.booking.dto.BookingWithoutDateDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.common.exception.DataNotFoundException;
import ru.practicum.shareit.common.exception.ValidationException;
import ru.practicum.shareit.common.validation.CheckDataValidation;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.util.BookingStatus;
import ru.practicum.shareit.util.State;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final CheckDataValidation validation;
    private final UserService userService;
    private final ItemService itemService;

    @Override
    public BookingDto createBooking(long userId, BookingDto bookingDto) {
        validation.bookingCheck(bookingDto);
        validation.checkDataTime(bookingDto.getStart(), bookingDto.getEnd());

        Booking booking = getBooking(userId, bookingDto);
        if (userId == booking.getItem().getId()) {
            throw new DataNotFoundException("Невозможно создать бронирование на свою вещь");
        }
        booking.setStatus(BookingStatus.WAITING);
        return BookingMapper.toBookingDto(bookingRepository.save(booking));
    }

    @Override
    public BookingWithoutDateDto updateBooking(long userId, long bookingId, boolean approved) {
        Booking booking = getBookingById(bookingId);
        validation.itemCheckUser(userId, booking.getItem());
        BookingStatus status = approved ? BookingStatus.APPROVED : BookingStatus.REJECTED;

        if (booking.getStatus().equals(status)) {
            throw new ValidationException("Статус изменения бронирования уже подтвержден");
        }
        booking.setStatus(status);
        return BookingMapper.toBookingWithoutDateDto(bookingRepository.save(booking));
    }

    @Override
    public BookingInfoDto findBookingById(long userId, long bookingId) {
        userService.findUserById(userId);
        Booking booking = getBookingById(bookingId);
        if (userId != booking.getBooker().getId() &&
                userId != booking.getItem().getOwner().getId()
        ) {
            throw new DataNotFoundException("Невозможно! Бронирование от другого пользователя");
        }
        return BookingMapper.toBookingInfoDto(booking);
    }

    @Override
    public List<BookingInfoDto> findAllBookingUser(long userId, String stateStr) {
        State state = checkUserAndState(userId, stateStr);
        List<Booking> bookings;
        LocalDateTime timeNow = LocalDateTime.now();

        switch (state) {
            case ALL:
                bookings = bookingRepository.findAllByBooker_IdOrderByStartDesc(userId);
                break;
            case CURRENT:
                bookings = bookingRepository.findAllByBookerCurrent(userId, timeNow);
                break;
            case PAST:
                bookings = bookingRepository.findAllByBooker_IdAndEndIsBeforeOrderByStartDesc(userId, timeNow);
                break;
            case FUTURE:
                bookings = bookingRepository.findAllByBooker_IdAndEndIsAfterOrderByStartDesc(userId, timeNow);
                break;
            default:
                BookingStatus bookingStatus = BookingStatus.valueOf(stateStr);
                bookings = bookingRepository.findAllByBooker_IdAndStatusOrderByStartDesc(userId, bookingStatus);
        }
        return bookings.stream()
                .map(BookingMapper::toBookingInfoDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<BookingInfoDto> findBookingAllItemsOwner(long userId, String stateStr) {
        State state = checkUserAndState(userId, stateStr);
        List<Booking> bookings;
        LocalDateTime timeNow = LocalDateTime.now();

        switch (state) {
            case ALL:
                bookings = bookingRepository.findAllByItem_Owner_IdOrderByStartDesc(userId);
                break;
            case CURRENT:
                bookings = bookingRepository.findAllByItemOwnerCurrent(userId, timeNow);
                break;
            case PAST:
                bookings = bookingRepository.findAllByItem_Owner_IdAndEndIsBeforeOrderByStartDesc(userId, timeNow);
                break;
            case FUTURE:
                bookings = bookingRepository.findAllByItem_Owner_IdAndEndIsAfterOrderByStartDesc(userId, timeNow);
                break;
            default:
                BookingStatus bookingStatus = BookingStatus.valueOf(stateStr);
                bookings = bookingRepository.findAllByItem_Owner_IdAndStatusOrderByStartDesc(userId, bookingStatus);
        }
        return bookings.stream()
                .map(BookingMapper::toBookingInfoDto)
                .collect(Collectors.toList());
    }

    private Item checkUserAndItem(long userId, BookingDto bookingDto) {
        User user = UserMapper.toUser(userService.findUserById(userId));

        Long itemId = bookingDto.getItemId();
        ItemDto itemDto = ItemMapper.toItemDto(itemService.getItemById(itemId));

        if (!itemDto.getAvailable()) {
            throw new ValidationException("Бронирование не доступно");
        }
        return ItemMapper.toItem(itemDto, user);
    }

    private Booking getBooking(Long userId, BookingDto bookingDto) {
        Item item = checkUserAndItem(userId, bookingDto);
        User user = UserMapper.toUser(userService.findUserById(userId));

        return BookingMapper.toBooking(bookingDto, user, item);
    }

    private Booking getBookingById(Long bookingId) {
        return bookingRepository.findById(bookingId)
                .orElseThrow(
                        () -> new DataNotFoundException(String.format("Бронирование %d не найден", bookingId))
                );
    }

    private State checkUserAndState(long userId, String stateStr) {
        userService.findUserById(userId);
        State state;
        try {
            state = State.valueOf(stateStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new ValidationException(String.format("Unknown state: %s", stateStr));
        }
        return state;
    }
}

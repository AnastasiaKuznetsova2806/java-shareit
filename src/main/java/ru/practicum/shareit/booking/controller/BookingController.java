package ru.practicum.shareit.booking.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingInfoDto;
import ru.practicum.shareit.booking.dto.BookingWithoutDateDto;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.common.validation.Pagination;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/bookings")
public class BookingController {
    private static final String STATE_DEFAULT = "ALL";
    private static final String USER_ID_HEADER = "X-Sharer-User-Id";
    private static final String DEFAULT_FROM = "0";
    private static final String DEFAULT_SIZE = "10";
    private final BookingService bookingService;

    @PostMapping
    public BookingDto createBooking(@RequestHeader(USER_ID_HEADER) long userId,
                                    @Valid @RequestBody BookingDto bookingDto) {
        log.info("Получен запрос на добавление объекта: '{}' ", bookingDto);
        return bookingService.createBooking(userId, bookingDto);
    }

    @PatchMapping("/{bookingId}")
    public BookingWithoutDateDto updateBooking(@RequestHeader(USER_ID_HEADER) long userId,
                                               @PathVariable long bookingId,
                                               @RequestParam("approved") Boolean approved) {
        log.info("Получен запрос на обновление объекта: '{}' ", bookingId);
        return bookingService.updateBooking(userId, bookingId, approved);
    }

    @GetMapping(value = "/{bookingId}")
    public BookingInfoDto findBookingById(@RequestHeader(USER_ID_HEADER) long userId,
                                          @PathVariable long bookingId) {
        return bookingService.findBookingById(userId, bookingId);
    }

    @GetMapping
    public List<BookingInfoDto> findAllBookingUser(
            @RequestHeader(USER_ID_HEADER) long userId,
            @RequestParam(value = "state", defaultValue = STATE_DEFAULT) String state,
            @PositiveOrZero @RequestParam(value = "from", defaultValue = DEFAULT_FROM) int from,
            @Positive @RequestParam(value = "size", defaultValue = DEFAULT_SIZE) int size) {
        Pageable pageable = Pagination.getPageable(from, size);
        return bookingService.findAllBookingUser(userId, state, pageable);
    }

    @GetMapping(value = "/owner")
    public List<BookingInfoDto> findBookingAllItemsOwner(
            @RequestHeader(USER_ID_HEADER) long userId,
            @RequestParam(value = "state", defaultValue = STATE_DEFAULT) String state,
            @PositiveOrZero @RequestParam(value = "from", defaultValue = DEFAULT_FROM) int from,
            @Positive @RequestParam(value = "size", defaultValue = DEFAULT_SIZE) int size) {
        Pageable pageable = Pagination.getPageable(from, size);
        return bookingService.findBookingAllItemsOwner(userId, state, pageable);
    }
}

package ru.practicum.shareit.booking.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingInfoDto;
import ru.practicum.shareit.booking.dto.BookingWithoutDateDto;
import ru.practicum.shareit.booking.service.BookingService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/bookings")
public class BookingController {
    private static final String STATE_DEFAULT = "ALL";
    private static final String USER_ID_HEADER = "X-Sharer-User-Id";
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
    public List<BookingInfoDto> findAllBookingUser(@RequestHeader(USER_ID_HEADER) long userId,
                                                   @RequestParam(value = "state",
                                                           defaultValue = STATE_DEFAULT) String state) {
        return bookingService.findAllBookingUser(userId, state);
    }

    @GetMapping(value = "/owner")
    public List<BookingInfoDto> findBookingAllItemsOwner(@RequestHeader(USER_ID_HEADER) long userId,
                                                         @RequestParam(value = "state",
                                                                 defaultValue = STATE_DEFAULT) String state) {
        return bookingService.findBookingAllItemsOwner(userId, state);
    }
}

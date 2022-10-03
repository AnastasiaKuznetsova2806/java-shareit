package ru.practicum.shareit.booking.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.State;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.common.CommonConstant;
import ru.practicum.shareit.common.exception.ValidationException;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/bookings")
public class BookingController {
    private static final String STATE_DEFAULT = "ALL";
    private final BookingClient bookingClient;

    @PostMapping
    public ResponseEntity<Object> createBooking(@RequestHeader(CommonConstant.USER_ID_HEADER) long userId,
                                                @Valid @RequestBody BookingDto bookingDto) {
        log.info("Получен запрос на добавление объекта: '{}' ", bookingDto);
        return bookingClient.createBooking(userId, bookingDto);
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> updateBooking(@RequestHeader(CommonConstant.USER_ID_HEADER) long userId,
                                               @PathVariable long bookingId,
                                               @RequestParam("approved") Boolean approved) {
        log.info("Получен запрос на обновление объекта: '{}', approved={} ", bookingId, approved);
        return bookingClient.updateBooking(userId, bookingId, approved);
    }

    @GetMapping(value = "/{bookingId}")
    public ResponseEntity<Object> findBookingById(@RequestHeader(CommonConstant.USER_ID_HEADER) long userId,
                                          @PathVariable long bookingId) {
        return bookingClient.findBookingById(userId, bookingId);
    }

    @GetMapping
    public ResponseEntity<Object> findAllBookingUser(
            @RequestHeader(CommonConstant.USER_ID_HEADER) long userId,
            @RequestParam(defaultValue = STATE_DEFAULT) String state,
            @PositiveOrZero @RequestParam(defaultValue =  CommonConstant.DEFAULT_FROM) int from,
            @Positive @RequestParam(defaultValue = CommonConstant.DEFAULT_SIZE) int size) {
        State bookingState = getBookingState(state);
        log.info("Получить все бронирования: state {}, userId={}, from={}, size={}", bookingState, userId, from, size);
        return bookingClient.findAllBookingUser(userId, bookingState, from, size);
    }

    @GetMapping(value = "/owner")
    public ResponseEntity<Object> findBookingAllItemsOwner(
            @RequestHeader(CommonConstant.USER_ID_HEADER) long userId,
            @RequestParam(defaultValue = STATE_DEFAULT) String state,
            @PositiveOrZero @RequestParam(defaultValue = CommonConstant.DEFAULT_FROM) int from,
            @Positive @RequestParam(defaultValue = CommonConstant.DEFAULT_SIZE) int size) {
        State bookingState = getBookingState(state);
        log.info("Получить владельца бронирования всех товаров: " +
                "state {}, userId={}, from={}, size={}", bookingState, userId, from, size);
        return bookingClient.findBookingAllItemsOwner(userId, bookingState, from, size);
    }

    private State getBookingState(String state) {
        return State.from(state)
                .orElseThrow(() -> new ValidationException(String.format("Unknown state: %s", state)));
    }
}

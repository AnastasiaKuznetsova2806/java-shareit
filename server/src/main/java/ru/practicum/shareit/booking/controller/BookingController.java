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
import ru.practicum.shareit.common.CommonConstant;
import ru.practicum.shareit.common.validation.PaginationUtil;

import java.util.List;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/bookings")
public class BookingController {
    private static final String STATE_DEFAULT = "ALL";
    private final BookingService bookingService;

    @PostMapping
    public BookingDto createBooking(@RequestHeader(CommonConstant.USER_ID_HEADER) long userId,
                                    @RequestBody BookingDto bookingDto) {
        log.info("Получен запрос на добавление объекта: '{}' ", bookingDto);
        return bookingService.createBooking(userId, bookingDto);
    }

    @PatchMapping("/{bookingId}")
    public BookingWithoutDateDto updateBooking(@RequestHeader(CommonConstant.USER_ID_HEADER) long userId,
                                               @PathVariable long bookingId,
                                               @RequestParam("approved") Boolean approved) {
        log.info("Получен запрос на обновление объекта: '{}' ", bookingId);
        return bookingService.updateBooking(userId, bookingId, approved);
    }

    @GetMapping(value = "/{bookingId}")
    public BookingInfoDto findBookingById(@RequestHeader(CommonConstant.USER_ID_HEADER) long userId,
                                          @PathVariable long bookingId) {
        return bookingService.findBookingById(userId, bookingId);
    }

    @GetMapping
    public List<BookingInfoDto> findAllBookingUser(
            @RequestHeader(CommonConstant.USER_ID_HEADER) long userId,
            @RequestParam(value = "state", defaultValue = STATE_DEFAULT) String state,
            @RequestParam(defaultValue =  CommonConstant.DEFAULT_FROM) int from,
            @RequestParam(defaultValue = CommonConstant.DEFAULT_SIZE) int size) {
        Pageable pageable = PaginationUtil.getPageable(from, size);
        return bookingService.findAllBookingUser(userId, state, pageable);
    }

    @GetMapping(value = "/owner")
    public List<BookingInfoDto> findBookingAllItemsOwner(
            @RequestHeader(CommonConstant.USER_ID_HEADER) long userId,
            @RequestParam(defaultValue = STATE_DEFAULT) String state,
            @RequestParam(defaultValue = CommonConstant.DEFAULT_FROM) int from,
            @RequestParam(defaultValue = CommonConstant.DEFAULT_SIZE) int size) {
        Pageable pageable = PaginationUtil.getPageable(from, size);
        return bookingService.findBookingAllItemsOwner(userId, state, pageable);
    }
}

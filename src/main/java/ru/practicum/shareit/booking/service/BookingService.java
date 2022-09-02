package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingInfoDto;
import ru.practicum.shareit.booking.dto.BookingWithoutDateDto;

import java.util.List;

public interface BookingService {
    BookingDto createBooking(long userId, BookingDto bookingDto);

    BookingWithoutDateDto updateBooking(long userId, long bookingId, boolean approved);

    BookingInfoDto findBookingById(long userId, long bookingId);

    List<BookingInfoDto> findAllBookingUser(long userId, String state);

    List<BookingInfoDto> findBookingAllItemsOwner(long userId, String state);
}

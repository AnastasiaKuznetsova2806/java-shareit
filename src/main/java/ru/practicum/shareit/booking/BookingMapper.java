package ru.practicum.shareit.booking;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingInfoDto;
import ru.practicum.shareit.booking.dto.BookingWithoutDateDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BookingMapper {
    public static BookingDto toBookingDto(Booking booking) {
        return new BookingDto(
                booking.getId(),
                booking.getStart(),
                booking.getEnd(),
                booking.getItem().getId(),
                booking.getBooker().getId(),
                booking.getStatus()
        );
    }

    public static BookingInfoDto toBookingInfoDto(Booking booking) {
        User user = booking.getBooker();
        Item item = booking.getItem();

        return new BookingInfoDto(
                booking.getStart(),
                booking.getEnd(),
                booking.getStatus(),
                new BookingInfoDto.BookerDTO(user.getId()),
                new BookingInfoDto.ItemForBookingDTO(item.getId(), item.getName()),
                booking.getId()
        );
    }

    public static BookingWithoutDateDto toBookingWithoutDateDto(Booking booking) {
        User user = booking.getBooker();
        Item item = booking.getItem();

        return new BookingWithoutDateDto(
                booking.getStatus(),
                new BookingWithoutDateDto.BookerDTO(user.getId()),
                new BookingWithoutDateDto.ItemForBookingDTO(item.getId(), item.getName()),
                booking.getId()
        );
    }

    public static Booking toBooking(BookingDto bookingDto, User user, Item item) {
        return new Booking(
                bookingDto.getId(),
                bookingDto.getStart() != null ? bookingDto.getStart() : null,
                bookingDto.getEnd() != null ? bookingDto.getEnd() : null,
                item,
                user,
                bookingDto.getStatus() != null ? bookingDto.getStatus() : null
        );
    }
}

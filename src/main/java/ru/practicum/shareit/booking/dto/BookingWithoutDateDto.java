package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.util.BookingStatus;

import javax.transaction.Transactional;

@Data
@Transactional
@AllArgsConstructor
public class BookingWithoutDateDto {
    private final BookingStatus status;
    private final BookerDTO booker;
    private final ItemForBookingDTO item;
    private long id;

    @Data
    public static class BookerDTO {
        private final long id;
    }

    @Data
    public static class ItemForBookingDTO {
        private final long id;
        private final String name;
    }
}

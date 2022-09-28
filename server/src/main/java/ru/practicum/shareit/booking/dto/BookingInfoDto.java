package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.util.BookingStatus;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class BookingInfoDto {
    private final LocalDateTime start;
    private final LocalDateTime end;
    private final BookingStatus status;
    private final BookerDTO booker;
    private final ItemForBookingDTO item;
    private long id;

    @Data
    @AllArgsConstructor
    public static class BookerDTO {
        private final Long id;
    }

    @Data
    @AllArgsConstructor
    public static class ItemForBookingDTO {
        private final Long id;
        private final String name;
    }
}

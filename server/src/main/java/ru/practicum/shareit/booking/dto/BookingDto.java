package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.util.BookingStatus;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class BookingDto implements Comparable<BookingDto> {
    private long id;
    private final LocalDateTime start;
    private final LocalDateTime end;
    private final Long itemId;
    private final Long booker;
    private final BookingStatus status;

    @Override
    public int compareTo(BookingDto o) {
        return this.getStart().isBefore(o.getStart()) ? -1 : 1;
    }
}

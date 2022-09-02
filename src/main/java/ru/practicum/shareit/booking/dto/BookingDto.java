package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.util.BookingStatus;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class BookingDto implements Comparable<BookingDto> {
    private long id;
    @NotNull(message = "Поле даты начала бронирования не может быть пустым")
    private final LocalDateTime start;

    @NotNull(message = "Поле даты конца бронирования не может быть пустым")
    private final LocalDateTime end;

    @NotNull(message = "Поле не может быть пустым")
    private final Long itemId;
    private final Long booker;
    private final BookingStatus status;

    @Override
    public int compareTo(BookingDto o) {
        return this.getStart().isBefore(o.getStart()) ? -1 : 1;
    }
}

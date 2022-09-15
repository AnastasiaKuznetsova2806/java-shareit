package ru.practicum.shareit.booking.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.util.BookingStatus;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Setter
@Entity
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "BOOKINGS")
public class Booking {
    @Id
    @Column(name = "ID_BOOKING")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "START_DATE")
    @JsonFormat(pattern = "YYYY-MM-DDTHH:mm:ss")
    private LocalDateTime start;

    @Column(name = "END_DATE")
    @JsonFormat(pattern = "YYYY-MM-DDTHH:mm:ss")
    private LocalDateTime end;

    @ManyToOne
    @JoinColumn(name = "ITEM_ID")
    private Item item;

    @ManyToOne
    @JoinColumn(name = "BOOKER_ID")
    private User booker;

    @Enumerated(value = EnumType.ORDINAL)
    private BookingStatus status;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Booking booking = (Booking) o;
        return id == booking.id &&
                Objects.equals(start, booking.start) &&
                Objects.equals(end, booking.end) &&
                Objects.equals(item, booking.item) &&
                Objects.equals(booker, booking.booker) &&
                status == booking.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, start, end, item, booker, status);
    }
}

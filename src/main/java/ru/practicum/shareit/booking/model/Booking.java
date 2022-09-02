package ru.practicum.shareit.booking.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.util.BookingStatus;

import javax.persistence.*;
import java.time.LocalDateTime;

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
}

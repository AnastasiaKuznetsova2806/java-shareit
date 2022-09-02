package ru.practicum.shareit.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.util.BookingStatus;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    //findAllByBooker
    List<Booking> findAllByBooker_IdOrderByStartDesc(long userId);

    @Query("select new ru.practicum.shareit.booking.model.Booking(" +
            " b.id," +
            "b.start," +
            "b.end," +
            "b.item," +
            "b.booker," +
            "b.status) " +
            "from Booking b " +
            "where b.booker.id = ?1 and ?2 between b.start and b.end " +
            "order by b.start desc ")
    List<Booking> findAllByBookerCurrent(long userId, LocalDateTime start);

    List<Booking> findAllByBooker_IdAndEndIsBeforeOrderByStartDesc(long userId, LocalDateTime start);

    List<Booking> findAllByBooker_IdAndEndIsAfterOrderByStartDesc(long userId, LocalDateTime start);

    List<Booking> findAllByBooker_IdAndStatusOrderByStartDesc(long userId, BookingStatus state);

    //findAllByItemOwner
    List<Booking> findAllByItem_Owner_IdOrderByStartDesc(long userId);

    @Query("select new ru.practicum.shareit.booking.model.Booking(" +
            " b.id," +
            "b.start," +
            "b.end," +
            "b.item," +
            "b.booker," +
            "b.status) " +
            "from Booking b " +
            "where b.item.owner.id = ?1 and ?2 between b.start and b.end " +
            "order by b.start desc ")
    List<Booking> findAllByItemOwnerCurrent(long userId, LocalDateTime start);

    List<Booking> findAllByItem_Owner_IdAndEndIsBeforeOrderByStartDesc(long userId, LocalDateTime start);

    List<Booking> findAllByItem_Owner_IdAndEndIsAfterOrderByStartDesc(long userId, LocalDateTime start);

    List<Booking> findAllByItem_Owner_IdAndStatusOrderByStartDesc(long userId, BookingStatus state);
}

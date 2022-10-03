package ru.practicum.shareit.booking.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.util.BookingStatus;

import java.time.LocalDateTime;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    //findAllByBooker
    Page<Booking> findAllByBooker_IdOrderByStartDesc(long userId, Pageable pageable);

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
    Page<Booking> findAllByBookerCurrent(long userId, LocalDateTime start, Pageable pageable);

    Page<Booking> findAllByBooker_IdAndEndIsBeforeOrderByStartDesc(
            long userId, LocalDateTime start, Pageable pageable);

    Page<Booking> findAllByBooker_IdAndEndIsAfterOrderByStartDesc(
            long userId, LocalDateTime start, Pageable pageable);

    Page<Booking> findAllByBooker_IdAndStatusOrderByStartDesc(
            long userId, BookingStatus state, Pageable pageable);

    //findAllByItemOwner
    Page<Booking> findAllByItem_Owner_IdOrderByStartDesc(long userId, Pageable pageable);

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
    Page<Booking> findAllByItemOwnerCurrent(long userId, LocalDateTime start, Pageable pageable);

    Page<Booking> findAllByItem_Owner_IdAndEndIsBeforeOrderByStartDesc(
            long userId, LocalDateTime start, Pageable pageable);

    Page<Booking> findAllByItem_Owner_IdAndEndIsAfterOrderByStartDesc(
            long userId, LocalDateTime start, Pageable pageable);

    Page<Booking> findAllByItem_Owner_IdAndStatusOrderByStartDesc(
            long userId, BookingStatus state, Pageable pageable);
}

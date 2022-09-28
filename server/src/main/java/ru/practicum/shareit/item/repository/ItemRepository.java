package ru.practicum.shareit.item.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {
    @Query("select new ru.practicum.shareit.booking.dto.BookingDto(" +
            "b.id, " +
            "b.start, " +
            "b.end, " +
            "b.item.id, " +
            "b.booker.id, " +
            "b.status) " +
            "from Booking b " +
            "where b.item.id = ?1 and b.booker.id <> ?2 " +
            "order by b.start")
    List<BookingDto> findBookingForItem(long itemId, long userId);

    Page<Item> findAllItemByOwner_Id(long userId, Pageable pageable);
}

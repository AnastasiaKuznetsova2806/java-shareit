package ru.practicum.shareit.item.comment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.comment.model.Comment;

import java.time.LocalDateTime;
import java.util.Set;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    Set<Comment> findAllByItemId(long itemId);

    @Query("select new ru.practicum.shareit.booking.model.Booking(" +
            "b.id, " +
            "b.start, " +
            "b.end, " +
            "b.item, " +
            "b.booker, " +
            "b.status) " +
            "from Booking b " +
            "where b.booker.id = ?1 and b.item.id = ?2 and b.end <= ?3 ")
    Booking getBookingFromUser(long userId, long itemId, LocalDateTime dateTime);
}

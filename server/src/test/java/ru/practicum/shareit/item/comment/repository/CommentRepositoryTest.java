package ru.practicum.shareit.item.comment.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.item.comment.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.util.BookingStatus;

import java.time.LocalDateTime;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class CommentRepositoryTest {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private CommentRepository commentRepository;

    final LocalDateTime timeNow = LocalDateTime.now().withNano(0);
    private User user;
    private Item item1;

    @BeforeEach
    void setUp() {
        user = userRepository.save(new User(1L, "user", "user@user.com"));
        item1 = itemRepository.save(new Item(
                1L, "item1", "1 description", true, user, null)
        );
    }

    @AfterEach
    void reset() {
        userRepository.deleteAll();
        bookingRepository.deleteAll();
        itemRepository.deleteAll();
        commentRepository.deleteAll();
    }

    @Test
    void test_1_findAllByItemId() {
        final Comment comment1 = commentRepository.save(new Comment(
                1L, "1 text", item1, user, timeNow)
        );
        final Comment comment2 = commentRepository.save(new Comment(
                2L, "2 text", item1, user, timeNow.plusDays(1))
        );

        Set<Comment> result = commentRepository.findAllByItemId(item1.getId());

        assertThat(result, notNullValue());
        assertEquals(2, result.size());
        assertThat(result, hasItem(comment1));
        assertThat(result, hasItem(comment2));
    }

    @Test
    void test_2_getBookingFromUser() {
        final Booking booking = bookingRepository.save(new Booking(
                        1L, timeNow, timeNow.plusDays(1), item1, user, BookingStatus.APPROVED)
        );

        Booking result = commentRepository.getBookingFromUser(user.getId(), item1.getId(), timeNow.plusDays(2));

        assertThat(result.getId(), notNullValue());
        assertThat(result.getStart(), equalTo(booking.getStart()));
        assertThat(result.getEnd(), equalTo(booking.getEnd()));
        assertThat(result.getBooker(), equalTo(booking.getBooker()));
        assertThat(result.getItem(), equalTo(booking.getItem()));
        assertThat(result.getStatus(), equalTo(booking.getStatus()));
    }
}
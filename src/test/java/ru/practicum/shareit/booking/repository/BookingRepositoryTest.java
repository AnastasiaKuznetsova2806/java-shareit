package ru.practicum.shareit.booking.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.util.BookingStatus;

import java.time.LocalDateTime;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class BookingRepositoryTest {
    private final LocalDateTime timeNow = LocalDateTime.now().withNano(0);
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private BookingRepository bookingRepository;
    private User user;
    private Booking booking1;
    private Booking booking2;

    @BeforeEach
    void setUp() {
        user = userRepository.save(new User(1L, "user", "user@user.com"));
        Item item = itemRepository.save(new Item(
                1L, "item1", "1 description", true, user, null)
        );
        booking1 = bookingRepository.save(new Booking(
                1L, timeNow, timeNow.plusDays(2), item, user, BookingStatus.APPROVED)
        );
        booking2 = bookingRepository.save(new Booking(
                2L, timeNow.minusDays(2), timeNow.minusDays(1), item, user, BookingStatus.WAITING)
        );
    }

    @Test
    void test_1_findAllByBooker_IdOrderByStartDesc() {
        Page<Booking> result = bookingRepository.findAllByBooker_IdOrderByStartDesc(
                user.getId(), PageRequest.of(0,2));

        assertThat(result, notNullValue());
        assertEquals(2, result.getTotalElements());
        assertThat(result, hasItem(booking1));
        assertThat(result, hasItem(booking2));
    }

    @Test
    void test_2_findAllByBookerCurrent() {
        Page<Booking> result = bookingRepository.findAllByBookerCurrent(
                user.getId(), timeNow, PageRequest.of(0,2));

        assertThat(result, notNullValue());
        assertEquals(1, result.getTotalElements());
        assertThat(result, hasItem(booking1));
    }

    @Test
    void test_3_findAllByBooker_IdAndEndIsBeforeOrderByStartDesc() {
        Page<Booking> result = bookingRepository.findAllByBooker_IdAndEndIsBeforeOrderByStartDesc(
                user.getId(), timeNow, PageRequest.of(0,2));

        assertThat(result, notNullValue());
        assertEquals(1, result.getTotalElements());
        assertThat(result, hasItem(booking2));
    }

    @Test
    void test_4_findAllByBooker_IdAndEndIsAfterOrderByStartDesc() {
        Page<Booking> result = bookingRepository.findAllByBooker_IdAndEndIsAfterOrderByStartDesc(
                user.getId(), timeNow, PageRequest.of(0,2));

        assertThat(result, notNullValue());
        assertEquals(1, result.getTotalElements());
        assertThat(result, hasItem(booking1));
    }

    @Test
    void test_5_findAllByBooker_IdAndStatusOrderByStartDesc() {
        Page<Booking> result = bookingRepository.findAllByBooker_IdAndStatusOrderByStartDesc(
                user.getId(), BookingStatus.WAITING, PageRequest.of(0,2));

        assertThat(result, notNullValue());
        assertEquals(1, result.getTotalElements());
        assertThat(result, hasItem(booking2));
    }

    @Test
    void test_6_findAllByItem_Owner_IdOrderByStartDesc() {
        Page<Booking> result = bookingRepository.findAllByItem_Owner_IdOrderByStartDesc(
                user.getId(), PageRequest.of(0,2));

        assertThat(result, notNullValue());
        assertEquals(2, result.getTotalElements());
        assertThat(result, hasItem(booking1));
        assertThat(result, hasItem(booking2));
    }

    @Test
    void test_7_findAllByItemOwnerCurrent() {
        Page<Booking> result = bookingRepository.findAllByItemOwnerCurrent(
                user.getId(), timeNow, PageRequest.of(0,2));

        assertThat(result, notNullValue());
        assertEquals(1, result.getTotalElements());
        assertThat(result, hasItem(booking1));
    }

    @Test
    void test_8_findAllByItem_Owner_IdAndEndIsBeforeOrderByStartDesc() {
        Page<Booking> result = bookingRepository.findAllByItem_Owner_IdAndEndIsBeforeOrderByStartDesc(
                user.getId(), timeNow, PageRequest.of(0,2));

        assertThat(result, notNullValue());
        assertEquals(1, result.getTotalElements());
        assertThat(result, hasItem(booking2));
    }

    @Test
    void test_9_findAllByItem_Owner_IdAndEndIsAfterOrderByStartDesc() {
        Page<Booking> result = bookingRepository.findAllByItem_Owner_IdAndEndIsAfterOrderByStartDesc(
                user.getId(), timeNow, PageRequest.of(0,2));

        assertThat(result, notNullValue());
        assertEquals(1, result.getTotalElements());
        assertThat(result, hasItem(booking1));
    }

    @Test
    void test_10_findAllByItem_Owner_IdAndStatusOrderByStartDesc() {
        Page<Booking> result = bookingRepository.findAllByItem_Owner_IdAndStatusOrderByStartDesc(
                user.getId(), BookingStatus.APPROVED, PageRequest.of(0,2));

        assertThat(result, notNullValue());
        assertEquals(1, result.getTotalElements());
        assertThat(result, hasItem(booking1));
    }

    @AfterEach
    void reset() {
        userRepository.deleteAll();
        bookingRepository.deleteAll();
        itemRepository.deleteAll();
    }
}
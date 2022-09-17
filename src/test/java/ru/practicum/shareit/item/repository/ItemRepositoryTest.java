package ru.practicum.shareit.item.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.booking.BookingMapper;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.util.BookingStatus;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ItemRepositoryTest {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private ItemRepository itemRepository;
    private User user;
    private Item item1;
    private Item item2;

    @BeforeEach
    void setUp() {
        user = userRepository.save(new User(1L, "user", "user@user.com"));
        item1 = itemRepository.save(new Item(
                1L, "item1", "1 description", true, user, null)
        );
        item2 = itemRepository.save(new Item(
                2L, "item2", "2 description", true, user, null)
        );
    }

    @AfterEach
    void reset() {
        userRepository.deleteAll();
        bookingRepository.deleteAll();
        itemRepository.deleteAll();
    }

    @Test
    void test_1_findBookingForItem() {
        final LocalDateTime timeNow = LocalDateTime.now().withNano(0);
        final BookingDto bookingDto = BookingMapper.toBookingDto(
                bookingRepository.save(new Booking(
                        1L, timeNow, timeNow.plusDays(2), item1, user, BookingStatus.APPROVED))
        );

        List<BookingDto> result = itemRepository.findBookingForItem(item1.getId(), 4L);

        assertThat(result, notNullValue());
        assertEquals(1, result.size());
        assertThat(result, hasItem(bookingDto));
    }

    @Test
    void test_2_findAllItemByOwner_Id() {
        Page<Item> result = itemRepository.findAllItemByOwner_Id(user.getId(), PageRequest.of(0,2));

        assertThat(result, notNullValue());
        assertEquals(2, result.getTotalElements());
        assertThat(result, hasItem(item2));
        assertThat(result, hasItem(item1));
    }
}
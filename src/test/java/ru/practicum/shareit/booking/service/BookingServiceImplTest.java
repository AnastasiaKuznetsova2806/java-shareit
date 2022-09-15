package ru.practicum.shareit.booking.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.booking.BookingMapper;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingInfoDto;
import ru.practicum.shareit.booking.dto.BookingWithoutDateDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.common.exception.DataNotFoundException;
import ru.practicum.shareit.common.exception.ValidationException;
import ru.practicum.shareit.common.validation.CheckDataValidation;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.util.BookingStatus;

import javax.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class BookingServiceImplTest {
    private BookingService bookingService;
    private BookingRepository bookingRepository;
    private UserService userService;
    private ItemService itemService;
    private UserDto userDto;
    private Item item;
    private Booking booking;
    private BookingDto bookingDto;
    private BookingInfoDto bookingInfoDto;
    private PageImpl<Booking> bookingPage;

    @BeforeEach
    void setUp() {
        CheckDataValidation validation = mock(CheckDataValidation.class);
        userService = mock(UserService.class);
        itemService = mock(ItemService.class);
        bookingRepository = mock(BookingRepository.class);
        bookingService = new BookingServiceImpl(
                bookingRepository, validation, userService, itemService
        );
        setParam();
    }

    @Test
    void test_1_createBooking() {
        when(itemService.getItemById(anyLong())).thenReturn(item);
        when(userService.findUserById(anyLong())).thenReturn(userDto);
        when(bookingRepository.save(any())).thenReturn(booking);

        BookingDto result = bookingService.createBooking(2L, bookingDto);

        assertThat(result.getId(), notNullValue());
        assertThat(result.getStart(), equalTo(bookingDto.getStart()));
        assertThat(result.getEnd(), equalTo(bookingDto.getEnd()));
        assertThat(result.getStatus(), equalTo(bookingDto.getStatus()));
        assertThat(result.getBooker(), equalTo(bookingDto.getBooker()));
    }

    @Test
    void test_2_createBookingDataNotFoundException() {
        when(itemService.getItemById(anyLong())).thenReturn(item);
        when(userService.findUserById(anyLong())).thenReturn(userDto);
        when(bookingRepository.save(any())).thenThrow(
                new DataNotFoundException("Невозможно создать бронирование на свою вещь")
        );

        final DataNotFoundException exception = assertThrows(
                DataNotFoundException.class,
                () -> bookingService.createBooking(2L, bookingDto)
        );

        assertEquals("Невозможно создать бронирование на свою вещь", exception.getMassage());
    }

    @Test
    void test_3_createBookingValidationException() {
        when(itemService.getItemById(anyLong())).thenReturn(item);
        when(userService.findUserById(anyLong())).thenReturn(userDto);
        when(bookingRepository.save(any())).thenThrow(
                new ValidationException("Бронирование не доступно")
        );

        final ValidationException exception = assertThrows(
                ValidationException.class,
                () -> bookingService.createBooking(2L, bookingDto)
        );

        assertEquals("Бронирование не доступно", exception.getMassage());
    }

    @Test
    void test_4_updateBooking() {
        final BookingWithoutDateDto bookingWithoutDateDto = new BookingWithoutDateDto(
                BookingStatus.REJECTED,
                new BookingWithoutDateDto.BookerDTO(1L),
                new BookingWithoutDateDto.ItemForBookingDTO(1L, "itemWithoutDate"),
                1L
        );
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.of(booking));
        when(bookingRepository.save(any())).thenReturn(booking);

        BookingWithoutDateDto result = bookingService.updateBooking(1L, 1L, false);

        assertThat(result.getId(), notNullValue());
        assertThat(result.getStatus(), equalTo(bookingWithoutDateDto.getStatus()));
        assertThat(result.getBooker(), equalTo(bookingWithoutDateDto.getBooker()));
    }

    @Test
    void test_5_updateBookingValidationException() {
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.of(booking));
        when(bookingRepository.save(any())).thenThrow(
                new ValidationException("Статус изменения бронирования уже подтвержден")
        );

        final ValidationException exception = assertThrows(
                ValidationException.class,
                () -> bookingService.updateBooking(1L, 1L, true)
        );

        assertEquals("Статус изменения бронирования уже подтвержден", exception.getMassage());
    }

    @Test
    void test_6_findBookingById() {
        when(userService.findUserById(anyLong())).thenReturn(userDto);
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.of(booking));

        BookingInfoDto result = bookingService.findBookingById(1L, 1L);

        assertThat(result.getId(), notNullValue());
        assertThat(result.getStart(), equalTo(bookingInfoDto.getStart()));
        assertThat(result.getEnd(), equalTo(bookingInfoDto.getEnd()));
        assertThat(result.getStatus(), equalTo(bookingInfoDto.getStatus()));
        assertThat(result.getBooker(), equalTo(bookingInfoDto.getBooker()));
    }

    @Test
    void test_7_findBookingByIdDataNotFoundException() {
        when(userService.findUserById(anyLong())).thenReturn(userDto);
        when(bookingRepository.findById(anyLong())).thenThrow(
                new DataNotFoundException("Невозможно! Бронирование от другого пользователя")
        );

        final DataNotFoundException exception = assertThrows(
                DataNotFoundException.class,
                () -> bookingService.findBookingById(1L, 1L)
        );

        assertEquals("Невозможно! Бронирование от другого пользователя", exception.getMassage());
    }

    @Test
    void test_8_findAllBookingUserAll() {
        when(bookingRepository.findAllByBooker_IdOrderByStartDesc(anyLong(), any())).thenReturn(bookingPage);

        List<BookingInfoDto> result = bookingService.findAllBookingUser(
                1L, "ALL", PageRequest.ofSize(10));

        assertThat(result, notNullValue());
        assertEquals(1, result.size());
        assertThat(result, hasItem(bookingInfoDto));
    }

    @Test
    void test_9_findAllBookingUserCurrent() {
        when(bookingRepository.findAllByBookerCurrent(anyLong(), any(), any())).thenReturn(bookingPage);

        List<BookingInfoDto> result = bookingService.findAllBookingUser(
                1L, "CURRENT", PageRequest.ofSize(10));

        assertThat(result, notNullValue());
        assertEquals(1, result.size());
        assertThat(result, hasItem(bookingInfoDto));
    }

    @Test
    void test_10_findAllBookingUserPast() {
        when(bookingRepository.findAllByBooker_IdAndEndIsBeforeOrderByStartDesc(anyLong(), any(), any()))
                .thenReturn(bookingPage);

        List<BookingInfoDto> result = bookingService.findAllBookingUser(
                1L, "PAST", PageRequest.ofSize(10));

        assertThat(result, notNullValue());
        assertEquals(1, result.size());
        assertThat(result, hasItem(bookingInfoDto));
    }

    @Test
    void test_11_findAllBookingUserFuture() {
        when(bookingRepository.findAllByBooker_IdAndEndIsAfterOrderByStartDesc(anyLong(), any(), any()))
                .thenReturn(bookingPage);

        List<BookingInfoDto> result = bookingService.findAllBookingUser(
                1L, "FUTURE", PageRequest.ofSize(10));

        assertThat(result, notNullValue());
        assertEquals(1, result.size());
        assertThat(result, hasItem(bookingInfoDto));
    }

    @Test
    void test_12_findAllBookingUserDefault() {
        when(bookingRepository.findAllByBooker_IdAndStatusOrderByStartDesc(anyLong(), any(), any()))
                .thenReturn(bookingPage);

        List<BookingInfoDto> result = bookingService.findAllBookingUser(
                1L, "REJECTED", Pageable.unpaged());

        assertThat(result, notNullValue());
        assertEquals(1, result.size());
        assertThat(result, hasItem(bookingInfoDto));
    }

    @Test
    void test_13_findAllBookingUserValidationException() {
        when(bookingRepository.findAllByBooker_IdAndStatusOrderByStartDesc(anyLong(), any(), any()))
                .thenThrow(
                        new ValidationException("Unknown state: UNSUPPORTED_STATUS")
                );

        final ValidationException exception = assertThrows(
                ValidationException.class,
                () -> bookingService.findAllBookingUser(
                        1L, "UNSUPPORTED_STATUS", PageRequest.ofSize(10))
        );

        assertEquals("Unknown state: UNSUPPORTED_STATUS", exception.getMassage());
    }

    @Test
    void test_14_findBookingAllItemsOwnerAll() {
        when(bookingRepository.findAllByItem_Owner_IdOrderByStartDesc(anyLong(), any())).thenReturn(bookingPage);

        List<BookingInfoDto> result = bookingService.findBookingAllItemsOwner(
                1L, "All", PageRequest.ofSize(10));

        assertThat(result, notNullValue());
        assertEquals(1, result.size());
        assertThat(result, hasItem(bookingInfoDto));
    }

    @Test
    void test_15_findBookingAllItemsOwnerCurrent() {
        when(bookingRepository.findAllByItemOwnerCurrent(anyLong(), any(), any())).thenReturn(bookingPage);

        List<BookingInfoDto> result = bookingService.findBookingAllItemsOwner(
                1L, "CURRENT", PageRequest.ofSize(10));

        assertThat(result, notNullValue());
        assertEquals(1, result.size());
        assertThat(result, hasItem(bookingInfoDto));
    }

    @Test
    void test_16_findBookingAllItemsOwnerPast() {
        when(bookingRepository.findAllByItem_Owner_IdAndEndIsBeforeOrderByStartDesc(anyLong(), any(), any()))
                .thenReturn(bookingPage);

        List<BookingInfoDto> result = bookingService.findBookingAllItemsOwner(
                1L, "PAST", PageRequest.ofSize(10));

        assertThat(result, notNullValue());
        assertEquals(1, result.size());
        assertThat(result, hasItem(bookingInfoDto));
    }

    @Test
    void test_17_findBookingAllItemsOwnerFuture() {
        when(bookingRepository.findAllByItem_Owner_IdAndEndIsAfterOrderByStartDesc(anyLong(), any(), any()))
                .thenReturn(bookingPage);

        List<BookingInfoDto> result = bookingService.findBookingAllItemsOwner(
                1L, "FUTURE", PageRequest.ofSize(10));

        assertThat(result, notNullValue());
        assertEquals(1, result.size());
        assertThat(result, hasItem(bookingInfoDto));
    }

    @Test
    void test_18_findBookingAllItemsOwnerDefault() {
        when(bookingRepository.findAllByItem_Owner_IdAndStatusOrderByStartDesc(anyLong(), any(), any()))
                .thenReturn(bookingPage);

        List<BookingInfoDto> result = bookingService.findBookingAllItemsOwner(
                1L, "REJECTED", PageRequest.ofSize(10));

        assertThat(result, notNullValue());
        assertEquals(1, result.size());
        assertThat(result, hasItem(bookingInfoDto));
    }

    @Test
    void test_19_findBookingAllItemsOwnerValidationException() {
        when(bookingRepository.findAllByItem_Owner_IdAndStatusOrderByStartDesc(anyLong(), any(), any()))
                .thenThrow(
                        new ValidationException("Unknown state: UNSUPPORTED_STATUS")
                );

        final ValidationException exception = assertThrows(
                ValidationException.class,
                () -> bookingService.findAllBookingUser(
                        1L, "UNSUPPORTED_STATUS", PageRequest.ofSize(10))
        );

        assertEquals("Unknown state: UNSUPPORTED_STATUS", exception.getMassage());
    }

    @Test
    void test_20_getBookingByIdDataNotFoundException() {
        when(bookingRepository.findById(anyLong()))
                .thenThrow(
                        new DataNotFoundException("Бронирование 1 не найден")
                );

        final DataNotFoundException exception = assertThrows(
                DataNotFoundException.class,
                () -> bookingService.findBookingById(1L, 1L)
        );

        assertEquals("Бронирование 1 не найден", exception.getMassage());
    }

    private void setParam() {
        LocalDateTime timeNow = LocalDateTime.now().withNano(0);
        User user = new User(1L,"user", "user@user.com");
        userDto = UserMapper.toUserDto(user);
        item = new Item(1L, "name", "description", true, user, 1L);
        booking = new Booking(1L, timeNow, timeNow.plusDays(2), item, user, BookingStatus.WAITING);
        bookingDto = BookingMapper.toBookingDto(booking);
        bookingInfoDto = new BookingInfoDto(
                timeNow,
                timeNow.plusDays(2),
                BookingStatus.WAITING,
                new BookingInfoDto.BookerDTO(1L),
                new BookingInfoDto.ItemForBookingDTO(1L, "name"),
                1L
        );
        bookingPage = new PageImpl<>(Collections.singletonList(booking));
    }
}
package ru.practicum.shareit.booking.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingInfoDto;
import ru.practicum.shareit.booking.dto.BookingWithoutDateDto;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.util.BookingStatus;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Collections;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = BookingController.class)
@AutoConfigureMockMvc
class BookingControllerTest {
    @Autowired
    private ObjectMapper mapper;
    @MockBean
    private BookingService bookingService;
    @Autowired
    private MockMvc mvc;
    private BookingDto bookingDto;
    private BookingWithoutDateDto bookingWithoutDateDto;
    private BookingInfoDto bookingInfoDto;

    @BeforeEach
    void setParam() {
        final LocalDateTime timeNow = LocalDateTime.now().withNano(0);
        bookingDto = new BookingDto(
                1L, timeNow, timeNow.plusDays(1), 1L, 1L, BookingStatus.WAITING);
        bookingWithoutDateDto = new BookingWithoutDateDto(
                BookingStatus.APPROVED,
                new BookingWithoutDateDto.BookerDTO(1L),
                new BookingWithoutDateDto.ItemForBookingDTO(1L, "itemWithoutDate"),
                1L
        );
        bookingInfoDto = new BookingInfoDto(
                timeNow,
                timeNow.plusDays(2),
                BookingStatus.WAITING,
                new BookingInfoDto.BookerDTO(1L),
                new BookingInfoDto.ItemForBookingDTO(1L, "itemInfo"),
                1L
        );
    }

    @Test
    void test_1_createBooking() throws Exception {
        when(bookingService.createBooking(anyLong(), any()))
                .thenReturn(bookingDto);

        mvc.perform(post("/bookings")
                        .content(mapper.writeValueAsString(bookingDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(bookingDto.getId()), Long.class))
                .andExpect(jsonPath("$.start", is(bookingDto.getStart().toString())))
                .andExpect(jsonPath("$.end", is(bookingDto.getEnd().toString())))
                .andExpect(jsonPath("$.itemId", is(bookingDto.getItemId()), Long.class))
                .andExpect(jsonPath("$.booker", is(bookingDto.getBooker()), Long.class));
    }

    @Test
    void test_2_updateBooking() throws Exception {
        createBooking(bookingDto);

        when(bookingService.updateBooking(anyLong(), anyLong(), anyBoolean()))
                .thenReturn(bookingWithoutDateDto);

        mvc.perform(patch("/bookings/{bookingId}", 1L)
                        .content(mapper.writeValueAsString(bookingWithoutDateDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1)
                        .param("approved", String.valueOf(true))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(bookingWithoutDateDto.getId()), Long.class))
                .andExpect(jsonPath("$.status", is(bookingWithoutDateDto.getStatus().toString())));
    }

    @Test
    void test_3_findBookingById() throws Exception {
        createBooking(bookingDto);

        when(bookingService.findBookingById(anyLong(), anyLong()))
                .thenReturn(bookingInfoDto);

        mvc.perform(get("/bookings/{bookingId}", 1L)
                        .content(mapper.writeValueAsString(bookingInfoDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(bookingInfoDto.getId()), Long.class))
                .andExpect(jsonPath("$.start", is(bookingInfoDto.getStart().toString())))
                .andExpect(jsonPath("$.end", is(bookingInfoDto.getEnd().toString())))
                .andExpect(jsonPath("$.status", is(bookingInfoDto.getStatus().toString())));
    }

    @Test
    void test_4_findAllBookingUser() throws Exception {
        createBooking(bookingDto);

        when(bookingService.findAllBookingUser(1L, "ALL", PageRequest.ofSize(10)))
                .thenReturn(Collections.singletonList(bookingInfoDto));

        mvc.perform(get("/bookings")
                        .content(mapper.writeValueAsString(bookingInfoDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(bookingInfoDto.getId()), Long.class))
                .andExpect(jsonPath("$[0].start", is(bookingInfoDto.getStart().toString())))
                .andExpect(jsonPath("$[0].end", is(bookingInfoDto.getEnd().toString())))
                .andExpect(jsonPath("$[0].status", is(bookingInfoDto.getStatus().toString())));

        verify(bookingService, times(1))
                .findAllBookingUser(1, "ALL", PageRequest.ofSize(10));
    }

    @Test
    void test_5_findBookingAllItemsOwner() throws Exception {
        createBooking(bookingDto);

        when(bookingService.findBookingAllItemsOwner(1L, "ALL", PageRequest.ofSize(10)))
                .thenReturn(Collections.singletonList(bookingInfoDto));

        mvc.perform(get("/bookings/owner")
                        .content(mapper.writeValueAsString(bookingInfoDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(bookingInfoDto.getId()), Long.class))
                .andExpect(jsonPath("$[0].start", is(bookingInfoDto.getStart().toString())))
                .andExpect(jsonPath("$[0].end", is(bookingInfoDto.getEnd().toString())))
                .andExpect(jsonPath("$[0].status", is(bookingInfoDto.getStatus().toString())));

        verify(bookingService, times(1))
                .findBookingAllItemsOwner(1, "ALL", PageRequest.ofSize(10));
    }

    private void createBooking(BookingDto bookingDto) {
        when(bookingService.createBooking(anyLong(), any()))
                .thenReturn(bookingDto);
    }
}
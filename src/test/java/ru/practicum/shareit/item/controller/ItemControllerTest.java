package ru.practicum.shareit.item.controller;

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
import ru.practicum.shareit.item.comment.dto.CommentInfoDto;
import ru.practicum.shareit.item.comment.servise.CommentService;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemInfoDto;
import ru.practicum.shareit.item.service.ItemService;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.*;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ItemController.class)
@AutoConfigureMockMvc
class ItemControllerTest {
    @Autowired
    private ObjectMapper mapper;
    @MockBean
    private ItemService itemService;
    @MockBean
    private CommentService commentService;
    @Autowired
    private MockMvc mvc;
    private CommentInfoDto commentInfoDto;
    private ItemDto itemDto;
    private ItemInfoDto itemInfoDto;

    @BeforeEach
    void setParam() {
        final LocalDateTime timeNow = LocalDateTime.now().withNano(0);
        commentInfoDto = new CommentInfoDto(1L, "textInfo", "authorName", timeNow);
        itemDto = new ItemDto(1L, "item", "description", true, 1L, 1L);
        itemInfoDto = new ItemInfoDto(
                "itemInfo",
                "descriptionInfo",
                true,
                new ItemInfoDto.BookingForItem(1L, 1L, timeNow, timeNow.plusDays(1)),
                new ItemInfoDto.BookingForItem(2L, 2L, timeNow, timeNow.plusDays(1)),
                getComments(),
                1L
        );
    }

    @Test
    void test_1_createItem() throws Exception {
        when(itemService.createItem(anyLong(), any()))
                .thenReturn(itemDto);

        mvc.perform(post("/items")
                        .content(mapper.writeValueAsString(itemDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemDto.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(itemDto.getName())))
                .andExpect(jsonPath("$.description", is(itemDto.getDescription())))
                .andExpect(jsonPath("$.available", is(itemDto.getAvailable())))
                .andExpect(jsonPath("$.owner", is(itemDto.getOwner()), Long.class))
                .andExpect(jsonPath("$.requestId", is(itemDto.getRequestId()), Long.class));
    }

    @Test
    void test_2_updateItem() throws Exception {
        createItem(itemDto);
        ItemDto itemDtoUpdate = new ItemDto(
                1L, "itemUpdate", "descriptionUpdate", true, 1L, 1L);

        when(itemService.updateItem(anyLong(), anyLong(), any()))
                .thenReturn(itemDtoUpdate);

        mvc.perform(patch("/items/{itemId}", 1L)
                        .content(mapper.writeValueAsString(itemDtoUpdate))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemDtoUpdate.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(itemDtoUpdate.getName())))
                .andExpect(jsonPath("$.description", is(itemDtoUpdate.getDescription())))
                .andExpect(jsonPath("$.available", is(itemDtoUpdate.getAvailable())))
                .andExpect(jsonPath("$.owner", is(itemDtoUpdate.getOwner()), Long.class))
                .andExpect(jsonPath("$.requestId", is(itemDtoUpdate.getRequestId()), Long.class));
    }

    @Test
    void test_3_findItemById() throws Exception {
        createItem(itemDto);

        when(itemService.findItemById(anyLong(), anyLong()))
                .thenReturn(itemInfoDto);

        mvc.perform(get("/items/{itemId}", 1L)
                .content(mapper.writeValueAsString(itemInfoDto))
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)
                .header("X-Sharer-User-Id", 1)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemInfoDto.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(itemInfoDto.getName())))
                .andExpect(jsonPath("$.description", is(itemInfoDto.getDescription())))
                .andExpect(jsonPath("$.available", is(itemInfoDto.getAvailable())))
                .andExpect(jsonPath("$.lastBooking.id", is(
                        itemInfoDto.getLastBooking().getId()), Long.class))
                .andExpect(jsonPath("$.nextBooking.id", is(
                        itemInfoDto.getNextBooking().getId()), Long.class));
    }

    @Test
    void test_4_findAllItemByUserId() throws Exception {
        createItem(itemDto);

        when(itemService.findAllItemByUserId(1, PageRequest.ofSize(10)))
                .thenReturn(Collections.singletonList(itemInfoDto));

        mvc.perform(get("/items")
                        .content(mapper.writeValueAsString(itemInfoDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(itemInfoDto.getId()), Long.class))
                .andExpect(jsonPath("$[0].name", is(itemInfoDto.getName())))
                .andExpect(jsonPath("$[0].description", is(itemInfoDto.getDescription())))
                .andExpect(jsonPath("$[0].available", is(itemInfoDto.getAvailable())))
                .andExpect(jsonPath("$[0].lastBooking.id", is(
                        itemInfoDto.getLastBooking().getId()), Long.class))
                .andExpect(jsonPath("$[0].nextBooking.id", is(
                        itemInfoDto.getNextBooking().getId()), Long.class));

        verify(itemService, times(1))
                .findAllItemByUserId(1, PageRequest.ofSize(10));
    }

    @Test
    void test_5_searchItem() throws Exception {
        createItem(itemDto);

        when(itemService.searchItem(1, "", PageRequest.ofSize(10)))
                .thenReturn(Collections.singletonList(itemDto));

        mvc.perform(get("/items/search")
                        .content(mapper.writeValueAsString(itemDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(itemDto.getId()), Long.class))
                .andExpect(jsonPath("$[0].name", is(itemDto.getName())))
                .andExpect(jsonPath("$[0].description", is(itemDto.getDescription())))
                .andExpect(jsonPath("$[0].available", is(itemInfoDto.getAvailable())));

        verify(itemService, times(1))
                .searchItem(1, "", PageRequest.ofSize(10));
    }

    @Test
    void test_6_deleteItemById() throws Exception {
        createItem(itemDto);
        doNothing().when(itemService).deleteItemById(1L, 1L);

        mvc.perform(delete("/items/{itemId}", 1L)
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk());
    }

    @Test
    void test_7_createComment() throws Exception {
        when(commentService.createComment(anyLong(), anyLong(), any()))
                .thenReturn(commentInfoDto);

        mvc.perform(post("/items/{itemId}/comment", 1L)
                        .content(mapper.writeValueAsString(commentInfoDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(commentInfoDto.getId()), Long.class))
                .andExpect(jsonPath("$.text", is(commentInfoDto.getText())))
                .andExpect(jsonPath("$.authorName", is(commentInfoDto.getAuthorName())))
                .andExpect(jsonPath("$.created", is(commentInfoDto.getCreated().toString())));
    }

    private void createItem(ItemDto itemDto) {
        when(itemService.createItem(anyLong(), any()))
                .thenReturn(itemDto);
    }

    private Set<CommentInfoDto> getComments() {
        return new HashSet<>(Collections.singletonList(commentInfoDto));
    }
}
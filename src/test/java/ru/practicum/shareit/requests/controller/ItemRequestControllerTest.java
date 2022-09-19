package ru.practicum.shareit.requests.controller;

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
import org.springframework.test.web.servlet.ResultActions;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.requests.dto.ItemRequestDto;
import ru.practicum.shareit.requests.dto.ItemRequestInfoDto;
import ru.practicum.shareit.requests.service.ItemRequestService;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ItemRequestController.class)
@AutoConfigureMockMvc
class ItemRequestControllerTest {
    @Autowired
    private ObjectMapper mapper;
    @MockBean
    private ItemRequestService itemRequestService;
    @Autowired
    private MockMvc mvc;
    private ItemRequestDto itemRequestDto;
    private ItemRequestInfoDto itemRequestInfoDto;

    @BeforeEach
    void setParam() {
        LocalDateTime timeNow = LocalDateTime.now().withNano(0);
        itemRequestDto = new ItemRequestDto(1L, "description", timeNow);
        itemRequestInfoDto = new ItemRequestInfoDto(1L, "description", timeNow, getItems());
    }

    @Test
    void test_1_createItemRequest() throws Exception {
        when(itemRequestService.createItemRequest(anyLong(), any()))
                .thenReturn(itemRequestDto);

        mvc.perform(post("/requests")
                        .content(mapper.writeValueAsString(itemRequestDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemRequestDto.getId()), Long.class))
                .andExpect(jsonPath("$.description", is(itemRequestDto.getDescription())))
                .andExpect(jsonPath("$.created", is(itemRequestDto.getCreated().toString())));
    }

    @Test
    void test_2_findAllItemRequest() throws Exception {
        createItemRequest(itemRequestDto);

        when(itemRequestService.findAllItemRequest(anyLong()))
                .thenReturn(Collections.singletonList(itemRequestInfoDto));

        contentCreation("", null, itemRequestInfoDto)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(itemRequestInfoDto.getId()), Long.class))
                .andExpect(jsonPath("$[0].description", is(itemRequestInfoDto.getDescription())))
                .andExpect(jsonPath("$[0].created", is(itemRequestInfoDto.getCreated().toString())));
    }

    @Test
    void test_3_findAllItemRequestByOtherUser() throws Exception {
        createItemRequest(itemRequestDto);

        when(itemRequestService.findAllItemRequestByOtherUser(1, PageRequest.ofSize(10)))
                .thenReturn(Collections.singletonList(itemRequestInfoDto));

        contentCreation("/all", null, itemRequestInfoDto)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(itemRequestInfoDto.getId()), Long.class))
                .andExpect(jsonPath("$[0].description", is(itemRequestInfoDto.getDescription())))
                .andExpect(jsonPath("$[0].created", is(itemRequestInfoDto.getCreated().toString())));

        verify(itemRequestService, times(1))
                .findAllItemRequestByOtherUser(1,  PageRequest.ofSize(10));
    }

    @Test
    void test_4_findItemRequestById() throws Exception {
        createItemRequest(itemRequestDto);

        when(itemRequestService.findItemRequestById(anyLong(), anyLong()))
                .thenReturn(itemRequestInfoDto);

        contentCreation("/{requestId}", 1L, itemRequestInfoDto)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemRequestInfoDto.getId()), Long.class))
                .andExpect(jsonPath("$.description", is(itemRequestInfoDto.getDescription())))
                .andExpect(jsonPath("$.created", is(itemRequestInfoDto.getCreated().toString())));
    }

    private void createItemRequest(ItemRequestDto itemRequestDto) {
        when(itemRequestService.createItemRequest(anyLong(), any()))
                .thenReturn(itemRequestDto);
    }

    private Set<ItemDto> getItems() {
        ItemDto itemDto = new ItemDto(
                1L, "item", "description", true, 1L, 1L);

        ItemDto itemDto2 = new ItemDto(
                2L, "item2", "description2", true, 1L, 2L);

        return new HashSet<>(Arrays.asList(itemDto, itemDto2));
    }

    private ResultActions contentCreation(String url, Long param, ItemRequestInfoDto dto) throws Exception {
        return mvc.perform(get("/requests" + url, param)
                .content(mapper.writeValueAsString(dto))
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)
                .header("X-Sharer-User-Id", 1)
                .accept(MediaType.APPLICATION_JSON));
    }
}
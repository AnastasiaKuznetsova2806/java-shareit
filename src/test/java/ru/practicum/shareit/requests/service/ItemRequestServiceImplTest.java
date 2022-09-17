package ru.practicum.shareit.requests.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.common.exception.DataNotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.requests.dto.ItemRequestDto;
import ru.practicum.shareit.requests.dto.ItemRequestInfoDto;
import ru.practicum.shareit.requests.model.ItemRequest;
import ru.practicum.shareit.requests.repository.ItemRequestRepository;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import javax.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class ItemRequestServiceImplTest {
    @Mock
    private ItemRequestService itemRequestService;
    @Mock
    private ItemRequestRepository itemRequestRepository;
    @Mock
    private UserService userService;
    private LocalDateTime timeNow;
    private UserDto userDto;
    private ItemRequestDto itemRequestDto;
    private ItemRequest itemRequest;
    private ItemRequestInfoDto itemRequestInfoDto;

    @BeforeEach
    void setUp() {
        itemRequestService = new ItemRequestServiceImpl(itemRequestRepository, userService);
        setParam();
    }

    @Test
    void test_1_createItemRequest() {
        when(userService.findUserById(anyLong())).thenReturn(userDto);
        when(itemRequestRepository.save(any())).thenReturn(itemRequest);

        ItemRequestDto result = itemRequestService.createItemRequest(1L, itemRequestDto);

        assertThat(result.getId(), notNullValue());
        assertThat(result.getDescription(), equalTo(itemRequestDto.getDescription()));
        assertThat(result.getCreated(), equalTo(itemRequestDto.getCreated()));
    }

    @Test
    void test_2_findAllItemRequest() {
        when(userService.findUserById(anyLong())).thenReturn(userDto);
        when(itemRequestRepository.findItemsByRequestId(anyLong())).thenReturn(getItems());
        when(itemRequestRepository.findItemRequestByRequestor_Id(anyLong()))
                .thenReturn(Collections.singletonList(itemRequest));

        List<ItemRequestInfoDto> result = itemRequestService.findAllItemRequest(1L);

        assertThat(result, notNullValue());
        assertEquals(1, result.size());
        assertThat(result, hasItem(itemRequestInfoDto));
    }

    @Test
    void test_3_findAllItemRequestByOtherUser() {
        ItemRequestDto itemRequestDto2 = new ItemRequestDto(1L, "description", timeNow.plusDays(1));
        final PageImpl<ItemRequestDto> itemRequestDtoPage = new PageImpl<>(
                Arrays.asList(itemRequestDto, itemRequestDto2));

        when(userService.findUserById(anyLong())).thenReturn(userDto);
        when(itemRequestRepository.findItemsByRequestId(anyLong())).thenReturn(getItems());
        when(itemRequestRepository.findAllItemRequestByOtherUser(1L, PageRequest.ofSize(10)))
                .thenReturn(itemRequestDtoPage);

        List<ItemRequestInfoDto> result = itemRequestService.findAllItemRequestByOtherUser(
                1L, PageRequest.ofSize(10));

        assertThat(result, notNullValue());
        assertEquals(2, result.size());
        assertThat(result, hasItem(itemRequestInfoDto));
    }

    @Test
    void test_4_findItemRequestById() {
        when(userService.findUserById(anyLong())).thenReturn(userDto);
        when(itemRequestRepository.findById(anyLong())).thenReturn(Optional.of(itemRequest));

        ItemRequestInfoDto result = itemRequestService.findItemRequestById(1L, 1L);

        assertThat(result.getId(), notNullValue());
        assertThat(result.getDescription(), equalTo(itemRequestInfoDto.getDescription()));
        assertThat(result.getCreated(), equalTo(itemRequestInfoDto.getCreated()));
    }

    @Test
    void test_5_findItemRequestByIdDataNotFoundException() {
        when(userService.findUserById(anyLong())).thenReturn(userDto);
        when(itemRequestRepository.findById(anyLong())).thenThrow(
                new DataNotFoundException("Запрос 20 не найден")
        );

        final DataNotFoundException exception = assertThrows(
                DataNotFoundException.class,
                () -> itemRequestService.findItemRequestById(1L, 20L)
        );

        assertEquals("Запрос 20 не найден", exception.getMassage());
    }

    private Set<ItemDto> getItems() {
        ItemDto itemDto = new ItemDto(
                1L, "item", "description", true, 1L, 1L);

        ItemDto itemDto2 = new ItemDto(
                2L, "item2", "description2", true, 1L, 2L);
        return new HashSet<>(Arrays.asList(itemDto, itemDto2));
    }

    private void setParam() {
        timeNow = LocalDateTime.now().withNano(0);
        User user = new User(1L, "user", "user@user.com");
        userDto = UserMapper.toUserDto(user);
        itemRequestDto = new ItemRequestDto(1L, "description", timeNow);
        itemRequest = new ItemRequest(1L,"description", user, timeNow);
        itemRequestInfoDto  = new ItemRequestInfoDto(1L, "description", timeNow, getItems());
    }
}
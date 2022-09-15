package ru.practicum.shareit.item.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.common.exception.DataNotFoundException;
import ru.practicum.shareit.common.validation.CheckDataValidation;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.comment.dto.CommentInfoDto;
import ru.practicum.shareit.item.comment.servise.CommentService;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemInfoDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.util.BookingStatus;

import javax.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Stream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class ItemServiceImplTest {
    private ItemService itemService;
    private ItemRepository itemRepository;
    private UserService userService;
    private CommentService commentService;
    private LocalDateTime timeNow;
    private User user;
    private UserDto userDto;
    private CommentInfoDto commentInfoDto;
    private Item item;
    private ItemDto itemDto;
    private ItemInfoDto itemInfoDto;

    @BeforeEach
    void setUp() {
        CheckDataValidation validation = mock(CheckDataValidation.class);
        userService = mock(UserService.class);
        commentService = mock(CommentService.class);
        itemRepository = mock(ItemRepository.class);
        itemService = new ItemServiceImpl(validation, itemRepository, userService, commentService);
        setParam();
    }

    @Test
    void test_1_createItem() {
        when(userService.findUserById(anyLong())).thenReturn(userDto);
        when(itemRepository.save(any())).thenReturn(item);
        ItemDto result = itemService.createItem(1L, itemDto);

        assertThat(result.getId(), notNullValue());
        assertThat(result.getName(), equalTo(itemDto.getName()));
        assertThat(result.getDescription(), equalTo(itemDto.getDescription()));
        assertThat(result.getAvailable(), equalTo(itemDto.getAvailable()));
        assertThat(result.getOwner(), equalTo(itemDto.getOwner()));
    }

    @Test
    void test_2_updateItem() {
        ItemDto itemDtoUpdate = new ItemDto(
                1L, "nameUpd", "descriptionUpd", false, 1L, 1L);
        Item itemUpdate = ItemMapper.toItem(itemDtoUpdate, user);

        when(userService.findUserById(anyLong())).thenReturn(userDto);
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));
        when(itemRepository.save(any())).thenReturn(itemUpdate);

        ItemDto result = itemService.updateItem(1L, 1L,  itemDtoUpdate);

        assertThat(result.getId(), notNullValue());
        assertThat(result.getName(), equalTo(itemDtoUpdate.getName()));
        assertThat(result.getDescription(), equalTo(itemDtoUpdate.getDescription()));
        assertThat(result.getAvailable(), equalTo(itemDtoUpdate.getAvailable()));
        assertThat(result.getOwner(), equalTo(itemDtoUpdate.getOwner()));
    }

    @Test
    void test_3_updateItemNullParam() {
        ItemDto itemDtoUpdate = new ItemDto(
                1L, "nameUpd", null, null, null, null
        );

        Item itemUpdate = new Item(
                1L, "nameUpd", "description", true, user, null
        );

        when(userService.findUserById(anyLong())).thenReturn(userDto);
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));
        when(itemRepository.save(any())).thenReturn(itemUpdate);

        ItemDto result = itemService.updateItem(1L, 1L,  itemDtoUpdate);

        assertThat(result.getId(), notNullValue());
        assertThat(result.getName(), equalTo(itemDtoUpdate.getName()));
        assertThat(result.getDescription(), equalTo(itemDto.getDescription()));
        assertThat(result.getAvailable(), equalTo(itemDto.getAvailable()));
        assertThat(result.getOwner(), equalTo(itemDto.getOwner()));
    }

    @Test
    void test_4_findItemById() {
        setItemInfo();
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));

        ItemInfoDto result = itemService.findItemById(1L, 1L);

        assertThat(result.getId(), notNullValue());
        assertThat(result.getName(), equalTo(itemInfoDto.getName()));
        assertThat(result.getDescription(), equalTo(itemInfoDto.getDescription()));
        assertThat(result.getAvailable(), equalTo(itemInfoDto.getAvailable()));
        assertThat(result.getLastBooking(), equalTo(itemInfoDto.getLastBooking()));
        assertThat(result.getNextBooking(), equalTo(itemInfoDto.getNextBooking()));
    }

    @Test
    void test_5_findAllItemByUserId() {
        final Item item2 = new Item(
                2L, "name2", "description2", true);
        final PageImpl<Item> itemPage = new PageImpl<>(Arrays.asList(item, item2));
        setItemInfo();
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));
        when(itemRepository.findAllItemByOwner_Id(1L, PageRequest.ofSize(10)))
                .thenReturn(itemPage);

        List<ItemInfoDto> result = itemService.findAllItemByUserId(1L, PageRequest.ofSize(10));

        assertThat(result, notNullValue());
        assertEquals(2, result.size());
        assertThat(result, hasItem(itemInfoDto));
    }

    @Test
    void test_6_findAllItem() {
        final PageImpl<Item> itemPage = new PageImpl<>(Collections.singletonList(item));
        when(itemRepository.findAll(PageRequest.ofSize(10))).thenReturn(itemPage);

        List<ItemDto> result = itemService.findAllItem(PageRequest.ofSize(10));

        assertThat(result, notNullValue());
        assertEquals(1, result.size());
        assertThat(result, hasItem(itemDto));
    }

    @Test
    void test_7_searchItem() {
        final PageImpl<Item> itemPage = new PageImpl<>(Collections.singletonList(item));
        when(itemRepository.findAll(PageRequest.ofSize(10))).thenReturn(itemPage);

        List<ItemDto> result = itemService.searchItem(1L, "des", PageRequest.ofSize(10));

        assertThat(result, notNullValue());
        assertEquals(1, result.size());
        assertThat(result, hasItem(itemDto));
    }

    @MethodSource("setParamForException")
    @ParameterizedTest(name = "{index} searchItemEmpty {0}")
    void test_8_searchItemEmpty(String param) {
        final PageImpl<Item> itemPage = new PageImpl<>(Collections.singletonList(item));
        when(itemRepository.findAll(PageRequest.ofSize(10))).thenReturn(itemPage);

        List<ItemDto> result = itemService.searchItem(1L, param, PageRequest.ofSize(10));

        assertThat(result, notNullValue());
        assertEquals(0, result.size());
    }

    @Test
    void test_9_deleteItemById() {
        when(itemRepository.save(any())).thenReturn(user);
        doNothing().when(itemRepository).deleteById(anyLong());

        final DataNotFoundException exception = assertThrows(
                DataNotFoundException.class,
                () -> itemService.findItemById(1L, 1L)
        );

        assertEquals(exception.getMassage(), "Объект 1 не найден");
    }

    @Test
    void test_10_getItemById() {
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));

        Item result = itemService.getItemById(1L);

        assertThat(result.getId(), notNullValue());
        assertThat(result.getName(), equalTo(item.getName()));
        assertThat(result.getDescription(), equalTo(item.getDescription()));
        assertThat(result.getAvailable(), equalTo(item.getAvailable()));
        assertThat(result.getOwner(), equalTo(item.getOwner()));
    }

    private Set<CommentInfoDto> getComments() {
        return new HashSet<>(Collections.singletonList(commentInfoDto));
    }

    private void setItemInfo() {
        BookingDto bookingDto = new BookingDto(
                1L, timeNow, timeNow.plusDays(1), 1L, 1L, BookingStatus.WAITING);
        BookingDto bookingDto2 = new BookingDto(
                2L, timeNow, timeNow.plusDays(1), 1L, 2L, BookingStatus.WAITING);

        when(itemRepository.findBookingForItem(anyLong(), anyLong()))
                .thenReturn(Arrays.asList(bookingDto, bookingDto2));
        when(commentService.findAllCommentsForItem(anyLong())).thenReturn(getComments());
    }

    private void setParam() {
        timeNow = LocalDateTime.now().withNano(0);
        user = new User(1L,"user", "user@user.com");
        userDto = UserMapper.toUserDto(user);
        commentInfoDto = new CommentInfoDto(1L, "textInfo", "authorName", timeNow);
        item = new Item(1L, "name", "description", true, user, null);
        itemDto = ItemMapper.toItemDto(item);
        itemInfoDto = new ItemInfoDto(
                "name",
                "description",
                true,
                new ItemInfoDto.BookingForItem(1L, 1L, timeNow, timeNow.plusDays(1)),
                new ItemInfoDto.BookingForItem(2L, 2L, timeNow, timeNow.plusDays(1)),
                getComments(),
                1L
        );
    }

    private Stream<Arguments> setParamForException() {
        return Stream.of(
                Arguments.of(""),
                Arguments.of(" ")
        );
    }
}
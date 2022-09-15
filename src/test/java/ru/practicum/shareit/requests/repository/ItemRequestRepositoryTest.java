package ru.practicum.shareit.requests.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.requests.ItemRequestMapper;
import ru.practicum.shareit.requests.dto.ItemRequestDto;
import ru.practicum.shareit.requests.model.ItemRequest;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ItemRequestRepositoryTest {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private ItemRequestRepository itemRequestRepository;
    private User user;
    private ItemRequest itemRequest1;
    private ItemRequest itemRequest2;

    @BeforeEach
    void setUp() {
        final LocalDateTime timeNow = LocalDateTime.now().withNano(0);

        user = userRepository.save(new User(1L, "user", "user@user.com"));
        itemRequest1 = itemRequestRepository.save(new ItemRequest(1L,"1 description", user, timeNow));
        itemRequest2 = itemRequestRepository.save(new ItemRequest(2L,"2 description", user, timeNow));
    }

    @Test
    void test_1_findItemRequestByRequestor_Id() {
        List<ItemRequest> result = itemRequestRepository.findItemRequestByRequestor_Id(user.getId());

        assertThat(result, notNullValue());
        assertEquals(2, result.size());
        assertThat(result, hasItem(itemRequest1));
        assertThat(result, hasItem(itemRequest2));
    }

    @Test
    void test_2_findItemsByRequestId() {
        final long id = itemRequest2.getId();
        final ItemDto itemDto1 = ItemMapper.toItemDto(
                itemRepository.save(new Item(
                        1L, "item1", "1 description", true, user, id))
        );
        final ItemDto itemDto2 = ItemMapper.toItemDto(
                itemRepository.save(new Item(
                        2L, "item2", "2 description", true, user, id))
        );

        Set<ItemDto> result = itemRequestRepository.findItemsByRequestId(id);

        assertThat(result, notNullValue());
        assertEquals(2, result.size());
        assertThat(result, hasItem(itemDto1));
        assertThat(result, hasItem(itemDto2));
    }

    @Test
    void test_3_findAllItemRequestByOtherUser() {
        final ItemRequestDto itemRequestDto2 = ItemRequestMapper.toItemRequestDto(itemRequest2);
        Page<ItemRequestDto> result = itemRequestRepository.findAllItemRequestByOtherUser(
                100L, PageRequest.of(1,1)
        );

        assertThat(result, notNullValue());
        assertEquals(2, result.getTotalElements());
        assertThat(result, hasItem(itemRequestDto2));
    }

    @AfterEach
    void reset() {
        userRepository.deleteAll();
        itemRepository.deleteAll();
        itemRequestRepository.deleteAll();
    }
}
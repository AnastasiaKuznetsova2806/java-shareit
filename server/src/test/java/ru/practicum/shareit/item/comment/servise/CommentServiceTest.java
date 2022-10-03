package ru.practicum.shareit.item.comment.servise;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.common.exception.ValidationException;
import ru.practicum.shareit.item.comment.CommentMapper;
import ru.practicum.shareit.item.comment.dto.CommentDto;
import ru.practicum.shareit.item.comment.dto.CommentInfoDto;
import ru.practicum.shareit.item.comment.model.Comment;
import ru.practicum.shareit.item.comment.repository.CommentRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.util.BookingStatus;

import javax.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class CommentServiceTest {
    @Mock
    private CommentRepository commentRepository;
    @Mock
    private CommentService commentService;
    @Mock
    private UserService userService;
    private LocalDateTime timeNow;
    private User user;
    private Item item;
    private Comment comment;
    private CommentInfoDto commentInfoDto;

    @BeforeEach
    void setUp() {
        commentService = new CommentService(commentRepository, userService);
        setParam();
    }

    @Test
    void test_1_findAllCommentsForItem() {
        Set<Comment> comments = new HashSet<>(Collections.singletonList(comment));
        when(commentRepository.findAllByItemId(anyLong())).thenReturn(comments);

        Set<CommentInfoDto> result = commentService.findAllCommentsForItem(1L);

        assertThat(result, notNullValue());
        assertEquals(1, result.size());
        assertThat(result, hasItem(commentInfoDto));
    }

    @Test
    void test_2_createComment() {
        final UserDto userDto = UserMapper.toUserDto(user);
        final Booking booking = new Booking(
                1L, timeNow, timeNow.plusDays(1), item, user, BookingStatus.WAITING);
        final CommentDto commentDto = new CommentDto(1L, "text");

        when(userService.findUserById(anyLong())).thenReturn(userDto);
        when(commentRepository.getBookingFromUser(anyLong(), anyLong(), any())).thenReturn(booking);
        when(commentRepository.save(any())).thenReturn(comment);

        CommentInfoDto result = commentService.createComment(1L, 1L, commentDto);

        assertThat(result.getId(), notNullValue());
        assertThat(result.getText(), equalTo(commentInfoDto.getText()));
        assertThat(result.getAuthorName(), equalTo(commentInfoDto.getAuthorName()));
        assertThat(result.getCreated(), equalTo(commentInfoDto.getCreated()));
    }

    @Test
    void test_3_checkBookerAndGetItemValidationException() {
        final UserDto userDto = UserMapper.toUserDto(user);
        final CommentDto commentDto = new CommentDto(1L, "text");

        when(userService.findUserById(anyLong())).thenReturn(userDto);
        when(commentRepository.getBookingFromUser(anyLong(), anyLong(), any())).thenReturn(null);
        when(commentRepository.save(any())).thenThrow(
                new ValidationException("Отзыв может оставить арендатор, после окончания срока аренды")
        );

        final ValidationException exception = assertThrows(
                ValidationException.class,
                () -> commentService.createComment(2L, 1L, commentDto)
        );

        assertEquals(
                "Отзыв может оставить арендатор, после окончания срока аренды", exception.getMassage()
        );
    }

    private void setParam() {
        timeNow = LocalDateTime.now().withNano(0);
        user = new User(1L,"user", "user@user.com");
        item = new Item(1L, "name", "description", true, user, 1L);
        comment = new Comment(1L, "text", item, user, timeNow);
        commentInfoDto = CommentMapper.toCommentInfoDto(comment);
    }
}
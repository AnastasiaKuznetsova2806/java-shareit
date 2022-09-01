package ru.practicum.shareit.item.comment.servise;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.common.exception.ValidationException;
import ru.practicum.shareit.item.comment.CommentMapper;
import ru.practicum.shareit.item.comment.dto.CommentDto;
import ru.practicum.shareit.item.comment.dto.CommentInfoDto;
import ru.practicum.shareit.item.comment.model.Comment;
import ru.practicum.shareit.item.comment.repository.CommentRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final UserService userService;

    public Set<CommentInfoDto> findAllCommentsForItem(long itemId) {
        return commentRepository.findAllByItemId(itemId).stream()
                .map(CommentMapper::toCommentInfoDto)
                .collect(Collectors.toSet());
    }

    public CommentInfoDto createComment(long userId, long itemId, CommentDto commentDto) {
        User user = UserMapper.toUser(userService.findUserById(userId));
        Item item = checkBookerAndGetItem(userId, itemId);

        Comment comment = CommentMapper.toComment(commentDto, item, user);
        return CommentMapper.toCommentInfoDto(commentRepository.save(comment));
    }

    private Item checkBookerAndGetItem(long userId, long itemId) {
        LocalDateTime timeNow = LocalDateTime.now();
        Booking booking = commentRepository.getBookingFromUser(userId, itemId, timeNow);
        if (booking == null) {
            throw new ValidationException("Отзыв может оставить арендатор, после окончания срока аренды");
        }
        return booking.getItem();
    }
}

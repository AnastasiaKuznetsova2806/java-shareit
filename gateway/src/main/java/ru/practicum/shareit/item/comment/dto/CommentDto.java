package ru.practicum.shareit.item.comment.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class CommentDto {
    private final LocalDateTime created = LocalDateTime.now();
    private long id;
    @NotNull(message = "Комментарий не может быть пустым")
    @NotBlank(message = "Комментарий не может быть пустым")
    private String text;
}

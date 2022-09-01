package ru.practicum.shareit.item.comment.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class CommentInfoDto {
    private long id;
    @NotNull(message = "Комментарий не может быть пустым")
    @NotBlank(message = "Комментарий не может быть пустым")
    private String text;

    @NotNull(message = "Поле автор может быть пустым")
    @NotBlank(message = "Поле автор может быть пустым")
    private String authorName;
    private LocalDateTime created;
}

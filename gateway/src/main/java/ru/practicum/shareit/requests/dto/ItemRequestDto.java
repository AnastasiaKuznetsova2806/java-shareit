package ru.practicum.shareit.requests.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ItemRequestDto {
    private long id;
    @NotNull(message = "Поле не может быть пустым")
    private final String description;
    private final LocalDateTime created;
}

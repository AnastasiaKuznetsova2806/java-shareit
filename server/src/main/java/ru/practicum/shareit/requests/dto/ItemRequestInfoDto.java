package ru.practicum.shareit.requests.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.item.dto.ItemDto;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@AllArgsConstructor
public class ItemRequestInfoDto {
    private long id;
    private final String description;
    private final LocalDateTime created;
    private final Set<ItemDto> items;
}

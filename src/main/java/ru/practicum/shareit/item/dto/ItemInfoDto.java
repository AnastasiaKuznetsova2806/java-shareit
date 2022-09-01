package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.item.comment.dto.CommentInfoDto;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@AllArgsConstructor
public class ItemInfoDto implements Comparable<ItemInfoDto> {
    private final String name;
    private final String description;
    private final Boolean available;
    private final BookingForItem lastBooking;
    private final BookingForItem nextBooking;
    private final Set<CommentInfoDto> comments;
    private long id;

    @Override
    public int compareTo(ItemInfoDto o) {
        int cmp;
        if (this.getLastBooking() == null) {
            cmp = 1;
        } else if (o.getLastBooking() == null) {
            cmp = -1;
        } else {
            cmp = (this.getLastBooking().getStart().isBefore(o.getLastBooking().getStart())) ? -1 : 1;
        }
        return cmp;
    }

    @Data
    public static class BookingForItem {
        private final long id;
        private final long bookerId;
        private final LocalDateTime start;
        private final LocalDateTime end;
    }
}

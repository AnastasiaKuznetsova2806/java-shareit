package ru.practicum.shareit.common.validation;

import lombok.experimental.UtilityClass;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@UtilityClass
public class PaginationUtil {
    public static Pageable getPageable(int from, int size) {
        int page = from / size;
        return PageRequest.of(page, size);
    }
}

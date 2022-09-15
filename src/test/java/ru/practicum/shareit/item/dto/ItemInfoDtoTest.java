package ru.practicum.shareit.item.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
class ItemInfoDtoTest {
    @Autowired
    private JacksonTester<ItemInfoDto> json;

    @Test
    public void test_1_ItemRequestInfoDto() throws Exception {
        final LocalDateTime timeNow = LocalDateTime.now().withNano(0);
        final ItemInfoDto itemDto = new ItemInfoDto(
                "name",
                "description",
                true,
                new ItemInfoDto.BookingForItem(1L, 1L, timeNow, timeNow.plusDays(1)),
                null,
                null,
                1L
        );
        JsonContent<ItemInfoDto> result = json.write(itemDto);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo("name");
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo("description");
        assertThat(result).extractingJsonPathBooleanValue("$.available").isEqualTo(true);
        assertThat(result).extractingJsonPathNumberValue("$.lastBooking.id").isEqualTo(1);
        assertThat(result).extractingJsonPathNumberValue("$.nextBooking.id").isEqualTo(null);
        assertThat(result).extractingJsonPathStringValue("$.comments").isNullOrEmpty();
    }
}
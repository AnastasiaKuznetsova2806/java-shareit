package ru.practicum.shareit.requests.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
class ItemRequestInfoDtoTest {
    @Autowired
    private JacksonTester<ItemRequestInfoDto> json;

    @Test
    public void test_1_ItemRequestInfoDto() throws Exception {
        final LocalDateTime timeNow = LocalDateTime.now().withNano(0);
        final ItemRequestInfoDto itemRequest = new ItemRequestInfoDto(1L, "description", timeNow, null);

        JsonContent<ItemRequestInfoDto> result = json.write(itemRequest);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo("description");
        assertThat(result).extractingJsonPathStringValue("$.created").isEqualTo(timeNow.toString());
        assertThat(result).extractingJsonPathStringValue("$.items").isNullOrEmpty();
    }
}
package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.item.dto.ItemInfoDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@SpringBootTest
class ItemMapperTest {

    @Test
    void toItemInfoDto() {
        User user = new User(1L,"user", "user@user.com");
        Item item = new Item(1L, "name", "description", true, user, 1L);

        ItemInfoDto result = ItemMapper.toItemInfoDto(item, null, null, null);

        assertThat(result.getId(), notNullValue());
        assertThat(result.getName(), equalTo(item.getName()));
        assertThat(result.getDescription(), equalTo(item.getDescription()));
        assertThat(result.getAvailable(), equalTo(item.getAvailable()));
        assertThat(result.getLastBooking(), nullValue());
        assertThat(result.getNextBooking(), nullValue());
        assertThat(result.getComments(), empty());
    }
}
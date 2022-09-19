package ru.practicum.shareit.common.validation;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.common.exception.DataNotFoundException;
import ru.practicum.shareit.common.exception.ValidationException;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CheckDataValidationTest {
    @Autowired
    private CheckDataValidation validation;

    @Test
    void test_1_userCheckDataNotFoundException() {
        final DataNotFoundException exception = assertThrows(
                DataNotFoundException.class,
                () -> validation.userCheck(null)
        );

        assertEquals("Пользователь не заполнен", exception.getMassage());
    }

    @MethodSource("setParamForException")
    @ParameterizedTest(name = "{index} userValidationNameValidationException {0}")
    void test_2_userValidationNameValidationException(String param) {
        final UserDto userDto = new UserDto(1L, param, "user@user.com");
        final ValidationException exception = assertThrows(
                ValidationException.class,
                () -> validation.userValidation(userDto)
        );

        assertEquals("Поле не может быть пустым", exception.getMassage());
    }

    @MethodSource("setParamForException")
    @ParameterizedTest(name = "{index} userValidationEmailValidationException {0}")
    void test_3_userValidationEmailValidationException(String param) {
        final UserDto userDto = new UserDto(1L, "user", param);
        final ValidationException exception = assertThrows(
                ValidationException.class,
                () -> validation.userValidation(userDto)
        );

        assertEquals("Поле не может быть пустым", exception.getMassage());
    }

    @Test
    void test_4_itemCheckDataNotFoundException() {
        final DataNotFoundException exception = assertThrows(
                DataNotFoundException.class,
                () -> validation.itemCheck(null)
        );

        assertEquals("Объект не заполнен", exception.getMassage());
    }

    @MethodSource("setParamForException")
    @ParameterizedTest(name = "{index} itemValidationNameValidationException {0}")
    void test_5_itemValidationNameValidationException(String param) {
        final ItemDto itemDto = new ItemDto(
                1L, param, "description", true, 1L, 1L
        );
        final ValidationException exception = assertThrows(
                ValidationException.class,
                () -> validation.itemValidation(itemDto)
        );

        assertEquals("Поле не может быть пустым", exception.getMassage());
    }

    @MethodSource("setParamForException")
    @ParameterizedTest(name = "{index} itemValidationDescriptionValidationException {0}")
    void test_6_itemValidationDescriptionValidationException(String param) {
        final ItemDto itemDto = new ItemDto(
                1L,"name", param, true, 1L, 1L
        );
        final ValidationException exception = assertThrows(
                ValidationException.class,
                () -> validation.itemValidation(itemDto)
        );

        assertEquals("Поле не может быть пустым", exception.getMassage());
    }

    @Test
    void test_7_itemValidationDAvailableValidationException() {
        final ItemDto itemDto = new ItemDto(
                1L, "name", "description", null, 1L, 1L
        );
        final ValidationException exception = assertThrows(
                ValidationException.class,
                () -> validation.itemValidation(itemDto)
        );

        assertEquals("Поле не может быть пустым", exception.getMassage());
    }

    @Test
    void test_8_itemCheckUserDataNotFoundException() {
        final User user = new User(1L, "user", "user@user.com");
        final ItemDto itemDto = new ItemDto(
                1L, "name", "description", true, 1L, 1L
        );
        final DataNotFoundException exception = assertThrows(
                DataNotFoundException.class,
                () -> validation.itemCheckUser(2L, ItemMapper.toItem(itemDto, user))
        );

        assertEquals("Редактировать вещь может только её владелец", exception.getMassage());
    }

    @Test
    void test_9_bookingCheckDataNotFoundException() {
        final DataNotFoundException exception = assertThrows(
                DataNotFoundException.class,
                () -> validation.bookingCheck(null)
        );

        assertEquals("Объект не заполнен", exception.getMassage());
    }

    @Test
    void test_10_checkDataTimeEndValidationException() {
        final LocalDateTime start = LocalDateTime.now().plusDays(1);
        final LocalDateTime end = LocalDateTime.now();
        final ValidationException exception = assertThrows(
                ValidationException.class,
                () -> validation.checkDataTime(start, end)
        );

        assertEquals(
                "Дата и время конца бронирования не может быть раньше начала бронирования",
                exception.getMassage()
        );
    }

    @Test
    void test_11_checkDataTimeStartValidationException() {
        final LocalDateTime start = LocalDateTime.now().minusYears(10);
        final LocalDateTime end = LocalDateTime.now();
        final ValidationException exception = assertThrows(
                ValidationException.class,
                () -> validation.checkDataTime(start, end)
        );

        assertEquals(
                "Дата и время начала бронирования не может быть в прошлом",
                exception.getMassage()
        );
    }

    private Stream<Arguments> setParamForException() {
        return Stream.of(
                Arguments.of(""),
                Arguments.of((Object) null)
        );
    }
}
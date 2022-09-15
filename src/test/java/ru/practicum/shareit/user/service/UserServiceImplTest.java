package ru.practicum.shareit.user.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.common.exception.DataNotFoundException;
import ru.practicum.shareit.common.validation.CheckDataValidation;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import javax.transaction.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class UserServiceImplTest {
    private final UserDto userDto = makeUserDto("user", "user@user.com");
    private final User user = UserMapper.toUser(userDto);
    private UserService userService;
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        CheckDataValidation validation = mock(CheckDataValidation.class);
        userService = new UserServiceImpl(userRepository, validation);
    }

    @Test
    void test_1_createUser() {
        when(userRepository.save(user)).thenReturn(user);
        UserDto userResult = userService.createUser(userDto);

        assertThat(userResult.getId(), notNullValue());
        assertThat(userResult.getName(), equalTo(userDto.getName()));
        assertThat(userResult.getEmail(), equalTo(userDto.getEmail()));
    }

    @Test
    void test_2_updateUser() {
        UserDto userDtoUpdate = makeUserDto("userUpdate", "userUpd@user.com");
        User userUpdate = UserMapper.toUser(userDtoUpdate);

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(userRepository.save(any())).thenReturn(userUpdate);

        UserDto userResult = userService.updateUser(1L, userDtoUpdate);

        assertThat(userResult.getId(), notNullValue());
        assertThat(userResult.getName(), equalTo(userDtoUpdate.getName()));
        assertThat(userResult.getEmail(), equalTo(userDtoUpdate.getEmail()));
    }

    @Test
    void test_3_findAllUsers() {
        when(userRepository.findAll()).thenReturn(Collections.singletonList(user));
        List<UserDto> users = userService.findAllUsers();

        assertThat(users, notNullValue());
        assertEquals(1, users.size());
        assertThat(users, hasItem(userDto));
    }

    @Test
    void test_4_findUserById() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        UserDto userResult = userService.findUserById(1L);

        assertThat(userResult.getId(), notNullValue());
        assertThat(userResult.getName(), equalTo(userResult.getName()));
        assertThat(userResult.getEmail(), equalTo(userResult.getEmail()));
    }

    @Test
    void test_5_deleteUserById() {
        when(userRepository.save(any())).thenReturn(user);
        doNothing().when(userRepository).deleteById(anyLong());

        final DataNotFoundException exception = assertThrows(
                DataNotFoundException.class,
                () -> userService.findUserById(1L)
        );

        assertEquals(exception.getMassage(), "Пользователь 1 не найден");
    }

    private UserDto makeUserDto(String name, String email) {
        return new UserDto(1L, name, email);
    }
}
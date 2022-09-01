package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.common.exception.DataNotFoundException;
import ru.practicum.shareit.common.validation.CheckDataValidation;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final CheckDataValidation validation;

    public UserDto createUser(UserDto userDto) {
        validation.userCheck(userDto);
        validation.userValidation(userDto);

        User user = UserMapper.toUser(userDto);
        return UserMapper.toUserDto(userRepository.save(user));
    }

    public UserDto updateUser(long userId, UserDto userDto) {
        validation.userCheck(userDto);

        User userOld = UserMapper.toUser(findUserById(userId));
        User userUpdate = UserMapper.toUser(userDto);

        User user = new User(
                userUpdate.getName() != null ? userUpdate.getName() : userOld.getName(),
                userUpdate.getEmail() != null ? userUpdate.getEmail() : userOld.getEmail()
        );
        user.setId(userId);
        return UserMapper.toUserDto(userRepository.save(user));
    }

    public List<UserDto> findAllUsers() {
        return userRepository.findAll().stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toList());
    }

    public UserDto findUserById(long userId) {
        if (userRepository.findById(userId).isEmpty()) {
            throw new DataNotFoundException(String.format("Пользователь %d не найден", userId));
        }
        User user = userRepository.findById(userId).get();
        return UserMapper.toUserDto(user);
    }

    public void deleteUserById(long userId) {
        userRepository.deleteById(userId);
    }
}

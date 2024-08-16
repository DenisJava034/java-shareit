package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.DuplicatedDataException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.Collection;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public Collection<UserDto> findAll() {
        return UserMapper.toUserDtoCollection(userRepository.findAll());
    }

    @Override
    public UserDto create(UserDto userDto) {
        validation(userDto);
        return UserMapper.toUserDto(userRepository.create(UserMapper.toUser(userDto)));
    }

    @Override
    public UserDto update(Long userId, UserDto userDto) {
        if (!userRepository.isUserExist(userId)) {
            throw new NotFoundException("User (id = " + userId + ") not found!");
        }
        userDto.setId(userId);
        return UserMapper.toUserDto(userRepository.update(UserMapper.toUser(userDto)));
    }

    @Override
    public UserDto getUserDtoById(Long userId) {
        return UserMapper.toUserDto(userRepository.findUserById(userId));
    }

    @Override
    public void delete(Long userId) {
        if (!userRepository.isUserExist(userId)) {
            throw new NotFoundException("User (id = " + userId + ") not found!");
        }
        userRepository.delete(userId);
    }

    private void validation(UserDto userDto) {
        if (userRepository.isUserEmailExist(userDto.getEmail())) {
            throw new DuplicatedDataException("E-Mail " + userDto.getEmail() + " is exist!");
        }
    }
}

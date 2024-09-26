package ru.practicum.shareit.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.Collection;

@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/users")
@Slf4j
public class UserController {
    private final UserService userService;

    @GetMapping
    public Collection<UserDto> findAll() {
        log.info(String.valueOf("Получение всех пользователей в Количестве: {}"), userService.findAll().size());
        return userService.findAll();
    }

    @GetMapping("/{userId}")
    public UserDto getUserDtoById(@PathVariable Long userId) {
        log.info(String.valueOf("Получение пользователя: {}"), userService.getUserDtoById(userId));
        return userService.getUserDtoById(userId);
    }

    @PatchMapping("/{userId}")
    public UserDto update(@PathVariable Long userId,
                          @RequestBody UserDto userDto) {
        log.info(String.valueOf("Обновление пользователя пользователя по id: {}"), userId);
        return userService.update(userId, userDto);
    }

    @PostMapping
    public UserDto create(@Valid @RequestBody UserDto userDto) {
        log.info(String.valueOf("Сохранение пользователя: {}"), userDto);
        return userService.create(userDto);
    }

    @DeleteMapping("/{userId}")
    public void delete(@PathVariable Long userId) {
      log.info(String.valueOf("Удаление пользователя с id: {}"), userId);
        userService.delete(userId);
    }
}

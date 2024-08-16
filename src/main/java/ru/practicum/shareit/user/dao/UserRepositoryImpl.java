package ru.practicum.shareit.user.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.DuplicatedDataException;
import ru.practicum.shareit.user.model.User;

import java.util.Collection;
import java.util.Map;

@RequiredArgsConstructor
@Repository
public class UserRepositoryImpl implements UserRepository {
    private Long index = 0L;
    private final Map<Long, User> users;

    @Override
    public Collection<User> findAll() {
        return users.values();
    }

    @Override
    public User create(User user) {
        user.setId(++index);
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User update(User newUser) {
        User user = users.get(newUser.getId());
        if (newUser.getEmail() != null
                && !newUser.getEmail().equals(user.getEmail())
                && isUserEmailExist(newUser.getEmail())) {
            throw new DuplicatedDataException("E-Mail " + newUser.getEmail() + " is exist!");
        }
        if (newUser.getEmail() != null
                && !newUser.getEmail().isBlank()) {
            user.setEmail(newUser.getEmail());
        }
        if (newUser.getName() != null
                && !newUser.getName().isBlank()) {
            user.setName(newUser.getName());
        }
        return user;
    }

    @Override
    public User findUserById(Long userId) {
        return users.get(userId);
    }

    @Override
    public boolean isUserEmailExist(String email) {
        return users.values().stream()
                .anyMatch(user -> user.getEmail().equals(email));
    }

    @Override
    public boolean isUserExist(Long userId) {
        return users.containsKey(userId);
    }

    @Override
    public void delete(Long userId) {
        users.remove(userId);
    }
}

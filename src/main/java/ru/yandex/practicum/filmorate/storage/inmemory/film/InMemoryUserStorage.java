package ru.yandex.practicum.filmorate.storage.inmemory.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.*;

@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage {

    private final Map<Long, User> userMap = new HashMap<>();

    @Override
    public List<User> findAllUsers() {
        return new ArrayList<>(userMap.values());
    }

    @Override
    public Optional<User> create(User user) {
        userMap.put(user.getId(), user);
        log.info("User added: {}", user.getName());
        return Optional.of(user);
    }

    @Override
    public Optional<User> update(User user) throws NotFoundException {
        if (!userMap.containsKey(user.getId())) {
            throw new NotFoundException("User not found");
        }
        userMap.put(user.getId(), user);
        log.info("User updated: {}", user.getName());
        return Optional.of(user);
    }

    @Override
    public Optional<User> getUserById(Long id) {
        return Optional.ofNullable(userMap.get(id));
    }
}

package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage {

    private final Map<Long, User> userMap = new HashMap<>();

    @Override
    public Map<Long, User> getUserMap() {
        return userMap;
    }

    @Override
    public List<User> findAllUsers() {
        return new ArrayList<>(userMap.values());
    }

    @Override
    public User create(User user) {
        userMap.put(user.getId(), user);
        log.info("User added: {}", user.getName());
        return user;
    }

    @Override
    public User update(User user) throws NotFoundException {
        if (!userMap.containsKey(user.getId())) {
            throw new NotFoundException("User not found");
        }
        userMap.put(user.getId(), user);
        log.info("User updated: {}", user.getName());
        return user;
    }

    @Override
    public User getUserById(Long id) throws NotFoundException {
        if (!userMap.containsKey(id)) {
            throw new NotFoundException(String.format("User with id %s not found", id));
        }
        return userMap.get(id);
    }
}

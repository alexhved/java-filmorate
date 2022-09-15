package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidateException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.validator.UserValidator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    private final UserValidator validator = new UserValidator();
    private final Map<Long, User> userMap = new HashMap<>();

    private static long idGenerator = 0;
    public static long generateId() {
        return ++idGenerator;
    }

    @GetMapping
    public List<User> allUsers() {
        return new ArrayList<>(userMap.values());
    }

    @PostMapping
    public User create(@RequestBody User user) throws ValidateException {
        validator.validate(user);
            userMap.put(user.getId(), user);
            log.info("User added: {}", user.getName());
        return user;
    }

    @PutMapping
    public User update(@RequestBody User user) throws ValidateException, NotFoundException {
        validator.validate(user);
        if (!userMap.containsKey(user.getId())) {
            throw new NotFoundException("User not found");
        }
            userMap.put(user.getId(), user);
            log.info("User updated: {}", user.getName());
        return user;
    }
}

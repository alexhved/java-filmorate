package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidateException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.user.UserService;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public User findUserById(@PathVariable Long id) throws NotFoundException {
        return userService.getUserById(id);
    }

    @GetMapping("/{id}/friends")
    public List<User> findFriends(@PathVariable Long id) throws NotFoundException {
        return userService.findFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> findMutualFriends(@PathVariable Long id, @PathVariable Long otherId) throws NotFoundException {
        return userService.findMutualFriends(id, otherId);
    }

    @GetMapping
    public List<User> findAllUsers() {
        return userService.findAllUsers();
    }

    @PostMapping
    public User create(@RequestBody User user) throws ValidateException, NotFoundException {
        return userService.create(user);
    }

    @PutMapping
    public User update(@RequestBody User user) throws ValidateException, NotFoundException {
        return userService.update(user);
    }

    @PutMapping("{id}/friends/{friendId}")
    public String addFriend(@PathVariable Long id, @PathVariable Long friendId) throws NotFoundException {
        return userService.addFriend(id, friendId);
    }

    @DeleteMapping("{id}/friends/{friendId}")
    public String delFriend(@PathVariable Long id, @PathVariable Long friendId) throws NotFoundException {
        return userService.delFriend(id, friendId);
    }
}

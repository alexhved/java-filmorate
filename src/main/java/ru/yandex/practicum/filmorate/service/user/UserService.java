package ru.yandex.practicum.filmorate.service.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.EntityValidator;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserService {

    private final UserStorage userStorage;

    private final EntityValidator<User> validator;

    @Autowired
    public UserService(UserStorage userStorage, EntityValidator<User> validator) {
        this.userStorage = userStorage;
        this.validator = validator;
    }

    public EntityValidator<User> getValidator() {
        return validator;
    }

    public UserStorage getUserStorage() {
        return userStorage;
    }

    public String addFriend(Long id, Long friendId) throws NotFoundException {
        User user = userStorage.getUserById(id);
        User otherUser = userStorage.getUserById(friendId);

        if (user == null) {
            throw new NotFoundException(String.format("User with id %s not found", id));
        }
        if (otherUser == null) {
            throw new NotFoundException(String.format("User with id %s not found", friendId));
        }

        user.getFriends().add(otherUser.getId());
        otherUser.getFriends().add(user.getId());

        log.info("{} added {} to friends", user.getName(), otherUser.getName());

        return String.format("%s added %s to friends", user.getName(), otherUser.getName());
    }

    public String delFriend(Long id, Long friendId) throws NotFoundException {
        User user = userStorage.getUserById(id);
        User otherUser = userStorage.getUserById(friendId);

        if (user == null) {
            throw new NotFoundException(String.format("User with id %s not found", id));
        }
        if (otherUser == null) {
            throw new NotFoundException(String.format("User with id %s not found", friendId));
        }
        if (!user.getFriends().contains(otherUser.getId()) || !otherUser.getFriends().contains(user.getId())) {
            throw new NotFoundException("Not found among your friends");
        }

        user.getFriends().remove(otherUser.getId());
        otherUser.getFriends().remove(user.getId());

        log.info("{} removed {} to friends", user.getName(), otherUser.getName());

        return String.format("%s deleted %s from friends", user.getName(), otherUser.getName());
    }

    public List<User> findFriends(Long id) throws NotFoundException {
        if (!userStorage.getUserMap().containsKey(id)) {
            throw new NotFoundException(String.format("User with id %s not found", id));
        }

        return userStorage.getUserById(id).getFriends().stream()
                .map(userId -> {
                    try {
                        return userStorage.getUserById(userId);
                    } catch (NotFoundException e) {
                        throw new RuntimeException(e);
                    }
                })
                .collect(Collectors.toList());
    }

    public List<User> findMutualFriends(Long id, Long otherId) throws NotFoundException {
        User user = userStorage.getUserById(id);
        User otherUser = userStorage.getUserById(otherId);

        if (user == null) {
            throw new NotFoundException(String.format("User with id %s not found", id));
        }
        if (otherUser == null) {
            throw new NotFoundException(String.format("User with id %s not found", otherId));
        }

        Set<Long> userFriends = user.getFriends();
        return otherUser.getFriends().stream()
                .filter(userFriends::contains)
                .map(friendId -> {
                    try {
                        return userStorage.getUserById(friendId);
                    } catch (NotFoundException e) {
                        throw new RuntimeException(e);
                    }
                })
                .collect(Collectors.toList());
    }
}


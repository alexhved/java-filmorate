package ru.yandex.practicum.filmorate.service.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidateException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.EntityValidator;
import ru.yandex.practicum.filmorate.storage.dao.UserDb;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserService {

    private final UserDb userStorage;

    private final EntityValidator<User> validator;

    @Autowired
    public UserService(@Qualifier("userDbStorage") UserDb userStorage, EntityValidator<User> validator) {
        this.userStorage = userStorage;
        this.validator = validator;
    }

    public String addFriend(Long id, Long friendId) throws NotFoundException {

        User user = userStorage.getUserById(id)
                .orElseThrow(() -> new NotFoundException(String.format("User with id %s not found", id)));

        User otherUser = userStorage.getUserById(friendId)
                .orElseThrow(() -> new NotFoundException(String.format("User with id %s not found", id)));

        userStorage.addFriend(id, friendId);
        
        user.getFriends().add(otherUser.getId());
        
        if (otherUser.getFriends().contains(user.getId())) {
            userStorage.addApprovedFriend(id, friendId);
            userStorage.deleteFriend(id, friendId);
            
            userStorage.addApprovedFriend(friendId, id);
            userStorage.deleteFriend(friendId, id);

            user.getApprovedFriends().add(otherUser.getId());
            user.getFriends().remove(otherUser.getId());

            otherUser.getApprovedFriends().add(user.getId());
            otherUser.getFriends().remove(otherUser.getId());
        }

        log.info("{} added {} to friends", user.getName(), otherUser.getName());

        return String.format("%s added %s to friends", user.getName(), otherUser.getName());
    }

    public String delFriend(Long id, Long friendId) throws NotFoundException {

        User user = userStorage.getUserById(id)
                .orElseThrow(() -> new NotFoundException(String.format("User with id %s not found", id)));

        User otherUser = userStorage.getUserById(friendId)
                .orElseThrow(() -> new NotFoundException(String.format("User with id %s not found", id)));

        if (!user.getFriends().contains(otherUser.getId())) {
            throw new NotFoundException("Not found among your friends");
        }

        user.getFriends().remove(otherUser.getId());

        userStorage.deleteFriend(id, friendId);

        log.info("{} removed {} to friends", user.getName(), otherUser.getName());
        return String.format("%s deleted %s from friends", user.getName(), otherUser.getName());
    }

    public List<User> findFriends(Long id) throws NotFoundException {

        User user = userStorage.getUserById(id)
                .orElseThrow(() -> new NotFoundException(String.format("User with id %s not found", id)));

        return user.getFriends().stream()
                .map(userId -> userStorage.getUserById(userId).orElseThrow())
                .collect(Collectors.toList());
    }

    public List<User> findMutualFriends(Long id, Long otherId) throws NotFoundException {

        User user = userStorage.getUserById(id)
                .orElseThrow(() -> new NotFoundException(String.format("User with id %s not found", id)));

        User otherUser = userStorage.getUserById(otherId)
                .orElseThrow(() -> new NotFoundException(String.format("User with id %s not found", id)));

        Set<Long> userFriends = user.getFriends();
        return otherUser.getFriends().stream()
                .filter(userFriends::contains)
                .map(friendId -> userStorage.getUserById(friendId).orElseThrow())
                .collect(Collectors.toList());
    }

    public User getUserById(Long id) throws NotFoundException {
        return userStorage.getUserById(id)
                .orElseThrow(() -> new NotFoundException(String.format("User with id %s not found", id)));
    }

    public List<User> findAllUsers() {
        return userStorage.findAllUsers();
    }

    public User create(User user) throws ValidateException, NotFoundException {
        validator.validate(user);
        return userStorage.create(user)
                .orElseThrow(() -> new NotFoundException(String.format("user %s creation error", user.getName())));
    }

    public User update(User user) throws NotFoundException, ValidateException {
        validator.validate(user);
        return userStorage.update(user)
                .orElseThrow(() -> new NotFoundException(String.format("user %s update error", user.getName())));
    }
}


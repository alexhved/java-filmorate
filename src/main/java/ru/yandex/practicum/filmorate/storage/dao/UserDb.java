package ru.yandex.practicum.filmorate.storage.dao;

import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;

public interface UserDb extends UserStorage {

    List<User> findFriends(Long id);

    void addFriend(Long id, Long friendId);

    void deleteFriend(long id, long friendId);

    void addApprovedFriend(Long id, Long friendId);
}

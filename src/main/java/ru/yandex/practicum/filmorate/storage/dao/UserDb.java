package ru.yandex.practicum.filmorate.storage.dao;

import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;

public interface UserDb extends UserStorage {

    List<User> findFriends(Long id);

    String addFriend(Long id, Long friendId);

    String deleteFriend(long id, long friendId);

    String addApprovedFriend(Long id, Long friendId);
}

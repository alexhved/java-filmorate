package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Map;

@Repository
public class UserDbStorage  implements  UserStorage {

    @Override
    public Map<Long, User> getUserMap() {
        return null;
    }

    @Override
    public List<User> findAllUsers() {
        return null;
    }

    @Override
    public User create(User user) {
        return null;
    }

    @Override
    public User update(User user) throws NotFoundException {
        return null;
    }

    @Override
    public User getUserById(Long id) throws NotFoundException {
        return null;
    }
}

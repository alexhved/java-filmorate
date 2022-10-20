package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Map;

@Repository
@Slf4j
public class UserDbStorage  implements  UserStorage {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate=jdbcTemplate;
    }

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

        String sqlQuery = "insert into users(name, email, login, birthday) " +
                "values (?, ?, ?, ?)";

        jdbcTemplate.update(sqlQuery,
                user.getName(),
                user.getEmail(),
                user.getLogin(),
                user.getStringBirthday());

        log.info("user with name {} added", user.getName());

        return user;
    }

    @Override
    public User update(User user) throws NotFoundException {

        String sqlQuery = "update users set name = ?, email = ?, login = ?, birthday = ? " +
                "where id = ?";

        jdbcTemplate.update(sqlQuery,
                user.getName(),
                user.getEmail(),
                user.getLogin(),
                user.getStringBirthday(),
                user.getId());

        log.info("user with name {} updated", user.getName());

        return user;
    }

    @Override
    public User getUserById(Long id) throws NotFoundException {
        return null;
    }
}

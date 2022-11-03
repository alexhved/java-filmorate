package ru.yandex.practicum.filmorate.storage.dao.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.dao.UserDb;

import java.time.LocalDate;
import java.util.*;

@Repository
@Slf4j
public class UserDbStorage implements UserDb {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate=jdbcTemplate;
    }

    @Override
    public List<User> findFriends(Long id) {
        String sql = "select * from USERS where ID in (select FRIEND_ID from FRIENDS where USER_ID = ?)";
        return jdbcTemplate.query(sql, new UserMapper(), id);
    }

    @Override
    public void addFriend(Long id, Long friendId) {
        String sql ="insert into FRIENDS values (?, ?)";
        jdbcTemplate.update(sql, id, friendId);
        log.info("user with id {} want add user with id {} to friends", id, friendId);
    }

    @Override
    public void deleteFriend(long id, long friendId) {
        String sql = "delete from FRIENDS where USER_ID = ? and FRIEND_ID = ?";
        jdbcTemplate.update(sql, id, friendId);
        log.info("user with id {} delete user with id {} from friends", id, friendId);
    }

    @Override
    public void addApprovedFriend(Long id, Long friendId) {
        String sql ="insert into APPROVED_FRIENDS values (?, ?)";
        jdbcTemplate.update(sql, id, friendId);
        log.info("user with id {} add user with id {} to friends", id, friendId);
    }

    @Override
    public List<User> findAllUsers() {
        String sql = "select * from users";
        List<User> users = jdbcTemplate.query(sql, (rs, rowNum) -> User.builder()
                        .id(rs.getLong("id"))
                        .name(rs.getString("name"))
                        .email(rs.getString("email"))
                        .login(rs.getString("login"))
                        .birthday(rs.getDate("birthday").toLocalDate())
                        .build()
        );

        String sqlFriends = "select * from friends";
        SqlRowSet rsFriends = jdbcTemplate.queryForRowSet(sqlFriends);

        while (rsFriends.next()) {
            for (User user : users) {
                if (rsFriends.getLong("user_id") == user.getId()) {
                    user.getFriends().add(rsFriends.getLong("friend_id"));
                }
            }
        }

        String sqlApprovedfriends = "select * from APPROVED_FRIENDS";
        SqlRowSet rsAppr0ved = jdbcTemplate.queryForRowSet(sqlApprovedfriends);

        while (rsAppr0ved.next()) {
            for (User user : users) {
                if (rsAppr0ved.getLong("user_id") == user.getId()) {
                    user.getApprovedFriends().add(rsAppr0ved.getLong("friend_id"));
                }
            }
        }

        return users;
    }

    @Override
    public Optional<User> create(User user) {
        String sqlQuery = "insert into users(name, email, login, birthday) " +
                "values (?, ?, ?, ?)";

        jdbcTemplate.update(sqlQuery,
                user.getName(),
                user.getEmail(),
                user.getLogin(),
                user.birthday());

        log.info("user with name {} added", user.getName());
        return getUserById(user.getId());
    }

    @Override
    public Optional<User> update(User user) throws NotFoundException {
        String sqlQuery = "update users set name = ?, email = ?, login = ?, birthday = ? " +
                "where id = ?";

        jdbcTemplate.update(sqlQuery,
                user.getName(),
                user.getEmail(),
                user.getLogin(),
                user.birthday(),
                user.getId());

        log.info("user with name {} updated", user.getName());
        return getUserById(user.getId());
    }

    @Override
    public Optional<User> getUserById(Long id) {
        String queryUser = "select * from USERS where id = ?";

        SqlRowSet userRows = jdbcTemplate.queryForRowSet(queryUser, id);

        if(userRows.next()) {
            User user = User.builder()
                    .id(userRows.getLong("id"))
                    .name(userRows.getString("name"))
                    .email(userRows.getString("email"))
                    .login(userRows.getString("login"))
                    .birthday(Objects.requireNonNull(userRows.getDate("birthday")).toLocalDate())
                    .build();

            user.getFriends().addAll(getFriendsById(user.getId()));
            user.getApprovedFriends().addAll(getApprovedFriendsById(user.getId()));

            log.info("found user: {} {}", user.getId(), user.getName());
            return Optional.of(user);
        } else {
            return Optional.empty();
        }
    }

    private Optional<User> getUserByEmail(User user) {
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("select * from USERS where EMAIL = ?", user.getEmail());

        if(userRows.next()) {
            long userId = userRows.getLong("id");
            String name = userRows.getString("name");
            String email = userRows.getString("email");
            String login = userRows.getString("login");
            LocalDate birthday = Objects.requireNonNull(userRows.getDate("birthday")).toLocalDate();

            User newUser = User.builder()
                    .id(userId)
                    .name(name)
                    .email(email)
                    .login(login)
                    .birthday(birthday)
                    .build();

            user.getFriends().addAll(getFriendsById(user.getId()));

            return Optional.of(newUser);
        } else {
            return Optional.empty();
        }
    }
    private Set<Long> getFriendsById(long id) {
        String queryFriends = "select FRIEND_ID from FRIENDS where USER_ID = ?";
        List<Long> friendsList = jdbcTemplate.query(queryFriends, (rs, rowNum) -> rs.getLong("friend_id"), id);
        return new HashSet<>(friendsList);
    }
    
    private Set<Long> getApprovedFriendsById(long id) {
        String sqlApprovedFriends = "select FRIEND_ID from APPROVED_FRIENDS where USER_ID = ?";
        List<Long> friendsList = jdbcTemplate.query(sqlApprovedFriends, (rs, rowNum) -> rs.getLong("friend_id"), id);
        return new HashSet<>(friendsList);
    }
}

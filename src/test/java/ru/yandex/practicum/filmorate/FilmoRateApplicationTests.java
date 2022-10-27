package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.dao.film.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.dao.user.UserDbStorage;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmoRateApplicationTests {
    private final UserDbStorage userStorage;
    private final FilmDbStorage filmDbStorage;

    @Test
    public void testBeans() {
        assertThat(userStorage).isNotNull();
        assertThat(filmDbStorage).isNotNull();
    }

    @Test
    @Sql(scripts = {"/schema.sql", "/data.sql", "/user/test-get-user.sql"})
    public void testGetUserById() {
        Optional<User> userOptional = userStorage.getUserById(1L);

        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user)
                                .hasFieldOrPropertyWithValue("id", 1L)
                                .hasFieldOrPropertyWithValue("name", "username")
                                .hasFieldOrPropertyWithValue("email", "user@mail.ru")
                                .hasFieldOrPropertyWithValue("login", "userlogin")
                                .hasFieldOrPropertyWithValue("birthday", LocalDate.of(2000, 1, 2))
                                .hasFieldOrPropertyWithValue("friends", Set.of())
                );
    }

    @Test
    @Sql(scripts = {"/schema.sql", "/data.sql", "/user/test-find-all-users.sql"})
    public void testFindAllUsers() {
        List<User> allUsers = userStorage.findAllUsers();

        assertThat(allUsers.get(0))
                .hasFieldOrPropertyWithValue("id", 1L)
                .hasFieldOrPropertyWithValue("name", "username")
                .hasFieldOrPropertyWithValue("email", "user@mail.ru")
                .hasFieldOrPropertyWithValue("login", "userlogin")
                .hasFieldOrPropertyWithValue("birthday", LocalDate.of(2000, 1, 2))
                .hasFieldOrPropertyWithValue("friends", Set.of());

        assertThat(allUsers.get(1))
                .hasFieldOrPropertyWithValue("id", 2L)
                .hasFieldOrPropertyWithValue("name", "friendname")
                .hasFieldOrPropertyWithValue("email", "friend@mail.ru")
                .hasFieldOrPropertyWithValue("login", "friendlogin")
                .hasFieldOrPropertyWithValue("birthday", LocalDate.of(1999, 1, 2))
                .hasFieldOrPropertyWithValue("friends", Set.of());
    }

    @Test
    @Sql(scripts = {"/schema.sql", "/data.sql", "/user/test-get-user.sql"})
    public void addFriend() {
        userStorage.addFriend(1L, 2L);
        Optional<User> userById = userStorage.getUserById(1L);

        assertThat(userById).isPresent().hasValueSatisfying(user ->
                assertThat(user)
                        .hasFieldOrPropertyWithValue("id", 1L)
                        .hasFieldOrPropertyWithValue("name", "username")
                        .hasFieldOrPropertyWithValue("email", "user@mail.ru")
                        .hasFieldOrPropertyWithValue("login", "userlogin")
                        .hasFieldOrPropertyWithValue("birthday", LocalDate.of(2000, 1, 2))
                        .hasFieldOrPropertyWithValue("friends", Set.of(2L)));
    }

    @Test
    @Sql(scripts = {"/schema.sql", "/data.sql", "/user/test-delete-friend.sql"})
    public void testDeleteFriend() {
        userStorage.deleteFriend(1,2);
        Optional<User> userById = userStorage.getUserById(1L);

        assertThat(userById).isPresent().hasValueSatisfying(user ->
                assertThat(user)
                        .hasFieldOrPropertyWithValue("id", 1L)
                        .hasFieldOrPropertyWithValue("name", "username")
                        .hasFieldOrPropertyWithValue("email", "user@mail.ru")
                        .hasFieldOrPropertyWithValue("login", "userlogin")
                        .hasFieldOrPropertyWithValue("birthday", LocalDate.of(2000, 1, 2))
                        .hasFieldOrPropertyWithValue("friends", Set.of()));

    }

    @Test
    @Sql(scripts = {"/schema.sql", "/data.sql", "/user/test-get-user.sql"})
    public void testUpdateUser() throws NotFoundException {
        Optional<User> userOpt = userStorage.update(User.builder()
                .id(1)
                .name("personname")
                .email("person@mail.ru")
                .login("personlogin")
                .birthday(LocalDate.of(2000, 1, 2))
                .build());

        assertThat(userOpt).isPresent().hasValueSatisfying(user ->
                assertThat(user)
                        .hasFieldOrPropertyWithValue("id", 1L)
                        .hasFieldOrPropertyWithValue("name", "personname")
                        .hasFieldOrPropertyWithValue("email", "person@mail.ru")
                        .hasFieldOrPropertyWithValue("login", "personlogin")
                        .hasFieldOrPropertyWithValue("birthday", LocalDate.of(2000, 1, 2))
                        .hasFieldOrPropertyWithValue("friends", Set.of()));
    }

    @Test
    @Sql(scripts = {"/schema.sql", "/data.sql", "/user/test-find-friends.sql"})
    public void testFindFriends() {
        List<User> friends = userStorage.findFriends(1L);

        assertThat(friends.get(0))
                .hasFieldOrPropertyWithValue("id", 2L)
                .hasFieldOrPropertyWithValue("name", "friendname")
                .hasFieldOrPropertyWithValue("email", "friend@mail.ru")
                .hasFieldOrPropertyWithValue("login", "friendlogin")
                .hasFieldOrPropertyWithValue("birthday", LocalDate.of(1999, 1, 2))
                .hasFieldOrPropertyWithValue("friends", Set.of());
    }

    @Test
    @Sql(scripts = {"/schema.sql", "/data.sql"})
    public void testCreateUser() {
        userStorage.create(User.builder()
                .name("username")
                .login("userlogin")
                .email("user@mail.ru")
                .birthday(LocalDate.of(2000, 1, 2))
                .build());

        Optional<User> userOptional = userStorage.getUserById(1L);

        assertThat(userOptional).isPresent().hasValueSatisfying(user -> assertThat(user)
                .hasFieldOrPropertyWithValue("id", 1L)
                .hasFieldOrPropertyWithValue("name", "username")
                .hasFieldOrPropertyWithValue("email", "user@mail.ru")
                .hasFieldOrPropertyWithValue("login", "userlogin")
                .hasFieldOrPropertyWithValue("birthday", LocalDate.of(2000, 1, 2))
                .hasFieldOrPropertyWithValue("friends", Set.of()));
    }

    @Test
    @Sql(scripts = {"/schema.sql", "/data.sql"})
    public void testGetUserByIdIsEmpty() {
        Optional<User> optionalUser = userStorage.getUserById(1L);

        assertThat(optionalUser).isEmpty();
    }

    @Test
    @Sql(scripts = {"/schema.sql", "/data.sql"})
    public void testCreateFilm() throws NotFoundException {
        filmDbStorage.create(
                        Film.builder()
                        .name("filmname")
                        .description("filmdescription")
                        .rate(1)
                        .releaseDate(LocalDate.of(2000, 1, 2))
                        .duration(90)
                        .mpa(new Mpa(1, "G"))
                        .build()
        );

        Optional<Film> optionalFilm = filmDbStorage.getFilmById(1L);

        assertThat(optionalFilm).isPresent().hasValueSatisfying(film ->
                assertThat(film)
                        .hasFieldOrPropertyWithValue("id", 1L)
                        .hasFieldOrPropertyWithValue("name", "filmname")
                        .hasFieldOrPropertyWithValue("description", "filmdescription")
                        .hasFieldOrPropertyWithValue("rate", 1)
                        .hasFieldOrPropertyWithValue("releaseDate", LocalDate.of(2000, 1, 2))
                        .hasFieldOrPropertyWithValue("duration", 90)
                        .hasFieldOrPropertyWithValue("mpa", new Mpa(1, "G")));
    }

    @Test
    @Sql(scripts = {"/schema.sql", "/data.sql", "/film/test-update-film.sql"})
    public void testUpdate() throws NotFoundException {
        filmDbStorage.update(
                Film.builder()
                .id(1L)
                .name("otherfilmname")
                .description("otherfilmdescription")
                .rate(2)
                .releaseDate(LocalDate.of(1999, 1, 2))
                .duration(60)
                .mpa(new Mpa(2, "PG"))
                .build()
        );

        Optional<Film> optionalFilm = filmDbStorage.getFilmById(1L);

        assertThat(optionalFilm).isPresent().hasValueSatisfying(film ->
                assertThat(film)
                        .hasFieldOrPropertyWithValue("id", 1L)
                        .hasFieldOrPropertyWithValue("name", "otherfilmname")
                        .hasFieldOrPropertyWithValue("description", "otherfilmdescription")
                        .hasFieldOrPropertyWithValue("rate", 2)
                        .hasFieldOrPropertyWithValue("releaseDate", LocalDate.of(1999, 1, 2))
                        .hasFieldOrPropertyWithValue("duration", 60)
                        .hasFieldOrPropertyWithValue("mpa", new Mpa(2, "PG")));
    }

    @Test
    @Sql(scripts = {"/schema.sql", "/data.sql", "/film/test-update-film.sql"})
    public void testGetFilmById() {
        Optional<Film> optionalFilm = filmDbStorage.getFilmById(1L);

        assertThat(optionalFilm).isPresent().hasValueSatisfying(film ->
                assertThat(film)
                        .hasFieldOrPropertyWithValue("id", 1L)
                        .hasFieldOrPropertyWithValue("name", "filmname")
                        .hasFieldOrPropertyWithValue("description", "filmdescription")
                        .hasFieldOrPropertyWithValue("rate", 1)
                        .hasFieldOrPropertyWithValue("releaseDate", LocalDate.of(2000, 1, 2))
                        .hasFieldOrPropertyWithValue("duration", 90)
                        .hasFieldOrPropertyWithValue("mpa", new Mpa(1, "G")));
    }

    @Test
    @Sql(scripts = {"/schema.sql", "/data.sql"})
    public void testGetFilmByIdIsEmpty() {
        Optional<Film> optionalFilm = filmDbStorage.getFilmById(1L);

        assertThat(optionalFilm).isEmpty();
    }

    @Test
    @Sql(scripts = {"/schema.sql", "/data.sql", "/film/test-add-like.sql"})
    public void testAddLike() {
        filmDbStorage.addLike(1L, 1L);

        Optional<Film> optionalFilm = filmDbStorage.getFilmById(1L);

        assertThat(optionalFilm).isPresent().hasValueSatisfying(film -> assertThat(film)
                .hasFieldOrPropertyWithValue("userLikes", Set.of(1L)));
    }

    @Test
    @Sql(scripts = {"/schema.sql", "/data.sql", "/film/test-remove-like.sql"})
    public void testRemoveLike() {
        filmDbStorage.removeLike(1L, 1L);

        Optional<Film> optionalFilm = filmDbStorage.getFilmById(1L);

        assertThat(optionalFilm).isPresent().hasValueSatisfying(film -> assertThat(film)
                .hasFieldOrPropertyWithValue("userLikes", Set.of()));
    }

    @Test
    @Sql(scripts = {"/schema.sql", "/data.sql", "/film/test-find-all-films.sql"})
    public void testFindAllFilms() {
        List<Film> filmList = filmDbStorage.findAllFilms();

        assertThat(filmList.get(0))
                .hasFieldOrPropertyWithValue("id", 1L)
                .hasFieldOrPropertyWithValue("name", "filmname")
                .hasFieldOrPropertyWithValue("description", "filmdescription")
                .hasFieldOrPropertyWithValue("rate", 1)
                .hasFieldOrPropertyWithValue("releaseDate", LocalDate.of(2000, 1, 2))
                .hasFieldOrPropertyWithValue("duration", 90)
                .hasFieldOrPropertyWithValue("mpa", new Mpa(1, "G"));

        assertThat(filmList.get(1))
                .hasFieldOrPropertyWithValue("id", 2L)
                .hasFieldOrPropertyWithValue("name", "otherfilmname")
                .hasFieldOrPropertyWithValue("description", "otherfilmdescription")
                .hasFieldOrPropertyWithValue("rate", 2)
                .hasFieldOrPropertyWithValue("releaseDate", LocalDate.of(2002, 1, 2))
                .hasFieldOrPropertyWithValue("duration", 60)
                .hasFieldOrPropertyWithValue("mpa", new Mpa(2, "PG"));
    }

    @Test
    @Sql(scripts = {"/schema.sql", "/data.sql"})
    public void testFindAllMpa() {
        List<Mpa> mpaList = filmDbStorage.findAllMpa();

        assertThat(mpaList.get(0))
                .hasFieldOrPropertyWithValue("id", 1L)
                .hasFieldOrPropertyWithValue("name", "G");
        assertThat(mpaList.get(1))
                .hasFieldOrPropertyWithValue("id", 2L)
                .hasFieldOrPropertyWithValue("name", "PG");
        assertThat(mpaList.get(2))
                .hasFieldOrPropertyWithValue("id", 3L)
                .hasFieldOrPropertyWithValue("name", "PG-13");
        assertThat(mpaList.get(3))
                .hasFieldOrPropertyWithValue("id", 4L)
                .hasFieldOrPropertyWithValue("name", "R");
        assertThat(mpaList.get(4))
                .hasFieldOrPropertyWithValue("id", 5L)
                .hasFieldOrPropertyWithValue("name", "NC-17");
    }

    @Test
    @Sql(scripts = {"/schema.sql", "/data.sql"})
    public void testFindMpaById() throws NotFoundException {
        Mpa mpaById = filmDbStorage.findMpaById(1);

        assertThat(mpaById)
                .hasFieldOrPropertyWithValue("id", 1L)
                .hasFieldOrPropertyWithValue("name", "G");
    }

    @Test
    @Sql(scripts = {"/schema.sql", "/data.sql"})
    public void testFindAllGenres() {
        List<Genre> allGenres = filmDbStorage.findAllGenres();

        assertThat(allGenres.get(0))
                .hasFieldOrPropertyWithValue("id", 1)
                .hasFieldOrPropertyWithValue("name", "Комедия");
        assertThat(allGenres.get(1))
                .hasFieldOrPropertyWithValue("id", 2)
                .hasFieldOrPropertyWithValue("name", "Драма");
        assertThat(allGenres.get(2))
                .hasFieldOrPropertyWithValue("id", 3)
                .hasFieldOrPropertyWithValue("name", "Мультфильм");
        assertThat(allGenres.get(3))
                .hasFieldOrPropertyWithValue("id", 4)
                .hasFieldOrPropertyWithValue("name", "Триллер");
        assertThat(allGenres.get(4))
                .hasFieldOrPropertyWithValue("id", 5)
                .hasFieldOrPropertyWithValue("name", "Документальный");
        assertThat(allGenres.get(5))
                .hasFieldOrPropertyWithValue("id", 6)
                .hasFieldOrPropertyWithValue("name", "Боевик");
    }

    @Test
    @Sql(scripts = {"/schema.sql", "/data.sql"})
    public void testFindGenreById() throws NotFoundException {
        Genre genreById = filmDbStorage.findGenreById(1);

        assertThat(genreById)
                .hasFieldOrPropertyWithValue("id", 1)
                .hasFieldOrPropertyWithValue("name", "Комедия");
    }
}

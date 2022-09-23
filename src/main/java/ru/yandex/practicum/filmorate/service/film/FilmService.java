package ru.yandex.practicum.filmorate.service.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidateException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.EntityValidator;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    private final EntityValidator<Film> validator;

    private static long idGenerator = 0;

    @Autowired
    public FilmService(FilmStorage filmStorage, UserStorage userStorage, EntityValidator<Film> validator) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
        this.validator = validator;
    }

    public static long generateId() {
        return ++idGenerator;
    }

    public Set<Long> addLike(Long filmId, Long userId) throws NotFoundException {
        Film film = filmStorage.getFilmById(filmId);
        User user = userStorage.getUserById(userId);

        Set<Long> userLikes = film.getUserLikes();
        userLikes.add(user.getId());

        log.info("{} like film {}", user.getName(), film.getName());

        return userLikes;
    }

    public Set<Long> delLike(Long filmId, Long userId) throws NotFoundException {
        Film film = filmStorage.getFilmById(filmId);
        User user = userStorage.getUserById(userId);

        Set<Long> userLikes = film.getUserLikes();
        if (!userLikes.contains(userId)) {
            throw new NotFoundException(String.format("like from user %s not found", user.getName()));
        }

        userLikes.remove(userId);

        log.info("{} unlike film {}", user.getName(), film.getName());

        return userLikes;
    }

    public List<Film> findPopularByCount(Long count) {
        List<Film> allFilms = filmStorage.findAllFilms();

        List<Film> films = allFilms.stream()
                .sorted((o1, o2) -> o2.getUserLikes().size() - o1.getUserLikes().size())
                .limit(count)
                .collect(Collectors.toList());

        return films;
    }

    public Film getFilmById(Long id) throws NotFoundException {
        return filmStorage.getFilmById(id);
    }

    public Film create(Film film) throws ValidateException {
        validator.validate(film);
        return filmStorage.create(film);
    }

    public List<Film> findAllFilms() {
        return filmStorage.findAllFilms();
    }

    public Film update(Film film) throws NotFoundException, ValidateException {
        validator.validate(film);
        return filmStorage.update(film);
    }
}

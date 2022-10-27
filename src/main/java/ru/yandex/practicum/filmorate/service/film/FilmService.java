package ru.yandex.practicum.filmorate.service.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidateException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.EntityValidator;
import ru.yandex.practicum.filmorate.storage.dao.FilmDb;
import ru.yandex.practicum.filmorate.storage.dao.UserDb;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FilmService {
    private final FilmDb filmStorage;
    private final UserDb userStorage;
    private final EntityValidator<Film> validator;

    @Autowired
    public FilmService(FilmDb filmStorage, UserDb userStorage, EntityValidator<Film> validator) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
        this.validator = validator;
    }

    public Set<Long> addLike(Long filmId, Long userId) throws NotFoundException {
        Film film = filmStorage.getFilmById(filmId)
                .orElseThrow(() -> new NotFoundException(String.format("Film with id %s not found", filmId)));
        User user = userStorage.getUserById(userId)
                .orElseThrow(() -> new NotFoundException(String.format("User with id %s not found", userId)));

        filmStorage.addLike(filmId, userId);

        Set<Long> userLikes = film.getUserLikes();
        userLikes.add(user.getId());

        log.info("{} like film {}", user.getName(), film.getName());

        return userLikes;
    }

    public Set<Long> delLike(Long filmId, Long userId) throws NotFoundException {
        Film film = filmStorage.getFilmById(filmId)
                .orElseThrow(() -> new NotFoundException(String.format("Film with id %s not found", filmId)));
        User user = userStorage.getUserById(userId)
                .orElseThrow(() -> new NotFoundException(String.format("User with id %s not found", userId)));

        Set<Long> userLikes = film.getUserLikes();
        if (!userLikes.contains(userId)) {
            throw new NotFoundException(String.format("like from user %s not found", user.getName()));
        }

        filmStorage.removeLike(filmId, userId);

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
        return filmStorage.getFilmById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Film with id %s not found", id)));
    }

    public Film create(Film film) throws ValidateException, NotFoundException {
        validator.validate(film);
        return filmStorage.create(film)
                .orElseThrow(() -> new NotFoundException("Film creation error"));
    }

    public List<Film> findAllFilms() {
        return filmStorage.findAllFilms();
    }

    public Film update(Film film) throws NotFoundException, ValidateException {
        validator.validate(film);
        filmStorage.getFilmById(film.getId())
                .orElseThrow(() -> new NotFoundException(String.format("Film with id %s not found", film.getId())));
        return filmStorage.update(film)
                .orElseThrow(() -> new NotFoundException("Film update error"));
    }

    public List<Mpa> findAllMpa() {
        return filmStorage.findAllMpa();
    }

    public Mpa findMpaById(int id) throws NotFoundException {
        return filmStorage.findMpaById(id);
    }

    public List<Genre> findAllGenres() {
        return filmStorage.findAllGenres();
    }

    public Genre findGenreById(int id) throws NotFoundException {
        return filmStorage.findGenreById(id);
    }
}

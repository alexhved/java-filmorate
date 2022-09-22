package ru.yandex.practicum.filmorate.service.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
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

    @Autowired
    public FilmService(FilmStorage filmStorage, UserStorage userStorage, EntityValidator<Film> validator) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
        this.validator = validator;
    }

    public EntityValidator<Film> getValidator() {
        return validator;
    }

    public FilmStorage getFilmStorage() {
        return filmStorage;
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
}
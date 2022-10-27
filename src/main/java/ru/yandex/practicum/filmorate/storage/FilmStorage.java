package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Optional;

public interface FilmStorage {
    List<Film> findAllFilms();
    Optional<Film> create(Film film) throws NotFoundException;
    Optional<Film> update(Film film) throws NotFoundException;
    Optional<Film> getFilmById(Long filmId) throws NotFoundException;
}

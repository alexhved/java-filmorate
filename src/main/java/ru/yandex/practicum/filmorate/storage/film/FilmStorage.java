package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {
    List<Film> findAllFilms();
    Film create(Film film);
    Film update(Film film) throws NotFoundException;
    Film getFilmById(Long filmId) throws NotFoundException;
}

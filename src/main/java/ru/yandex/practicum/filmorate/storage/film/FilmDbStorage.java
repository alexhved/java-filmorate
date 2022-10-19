package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

@Repository
public class FilmDbStorage implements FilmStorage {
    @Override
    public List<Film> findAllFilms() {
        return null;
    }

    @Override
    public Film create(Film film) {
        return null;
    }

    @Override
    public Film update(Film film) throws NotFoundException {
        return null;
    }

    @Override
    public Film getFilmById(Long filmId) throws NotFoundException {
        return null;
    }
}

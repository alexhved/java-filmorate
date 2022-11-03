package ru.yandex.practicum.filmorate.storage.dao;

import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.List;

public interface FilmDb extends FilmStorage {
    void addLike(Long filmId, Long userId);
    void removeLike(Long filmId, Long userId);

    List<Mpa> findAllMpa();

    Mpa findMpaById(int id) throws NotFoundException;

    List<Genre> findAllGenres();

    Genre findGenreById(int id) throws NotFoundException;
}

package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {

    private final Map<Long, Film> filmMap = new HashMap<>();

    @Override
    public List<Film> findAllFilms() {
        return new ArrayList<>(filmMap.values());
    }

    @Override
    public Film create(Film film) {
        filmMap.put(film.getId(), film);
        log.info("New film added: {}", film.getName());
        return film;
    }

    @Override
    public Film update(Film film) throws NotFoundException {
        if (!filmMap.containsKey(film.getId())) {
            throw new NotFoundException("Film not found");
        }
        filmMap.put(film.getId(), film);
        log.info("Film updated: {}", film.getName());
        return film;
    }

    @Override
    public Film getFilmById(Long filmId) throws NotFoundException {
        if (!filmMap.containsKey(filmId)) {
            throw new NotFoundException("Film not found");
        }
        return filmMap.get(filmId);
    }
}

package ru.yandex.practicum.filmorate.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidateException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.validator.FilmValidator;

import java.util.*;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    private final FilmValidator validator = new FilmValidator();
    private final Map<Long, Film> filmMap = new HashMap<>();

    @GetMapping()
    public List<Film> allFilms() {
        return new ArrayList<>(filmMap.values());
    }

    @PostMapping()
    public Film create(@RequestBody Film film) throws ValidateException {
        if (validator.validate(film)) {
            filmMap.put(film.getId(), film);
            log.info("Добавлен фильм  {}", film.getName());
        } else {
            throw new ValidateException("film is not valid");
        }
        return film;
    }

    @PutMapping()
    public Film update(@RequestBody Film film) throws ValidateException {
        if (validator.validate(film) && filmMap.containsKey(film.getId())) {
            filmMap.put(film.getId(), film);
            log.info("Обновлён фильм  {}", film.getName());

        } else {
            throw new ValidateException("film is not valid");
        }
        return film;
    }
}

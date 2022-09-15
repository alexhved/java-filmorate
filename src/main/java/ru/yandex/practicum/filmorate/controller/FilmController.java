package ru.yandex.practicum.filmorate.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidateException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.validator.FilmValidator;

import javax.validation.Valid;
import java.util.*;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    private final FilmValidator validator = new FilmValidator();
    private final Map<Long, Film> filmMap = new HashMap<>();

    private static long idGenerator = 0;

    public static long generateId() {
        return ++idGenerator;
    }

    @GetMapping()
    public List<Film> allFilms() {
        return new ArrayList<>(filmMap.values());
    }

    @PostMapping()
    public Film create(@Valid @RequestBody Film film) throws ValidateException {
        validator.validate(film);
        filmMap.put(film.getId(), film);
        log.info("New film added: {}", film.getName());
        return film;
    }

    @PutMapping()
    public Film update(@Valid @RequestBody Film film) throws ValidateException, NotFoundException {
        if (!filmMap.containsKey(film.getId())) {
            throw new NotFoundException("Film not found");
        }
        validator.validate(film);
        filmMap.put(film.getId(), film);
        log.info("Film updated: {}", film.getName());
        return film;
    }
}

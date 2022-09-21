package ru.yandex.practicum.filmorate.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidateException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.EntityValidator;
import ru.yandex.practicum.filmorate.service.film.FilmService;
import ru.yandex.practicum.filmorate.service.film.FilmValidator;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/films")
public class FilmController {
    private final FilmStorage filmStorage;
    private final FilmService filmService;
    private final EntityValidator<Film> validator;

    @Autowired
    public FilmController(FilmStorage filmStorage, FilmService filmService, FilmValidator validator) {
        this.filmStorage = filmStorage;
        this.filmService = filmService;
        this.validator = validator;
    }

    @GetMapping("/{id}")
    public Film findById(@PathVariable Long id) throws NotFoundException {
        return filmStorage.getFilmById(id);
    }

    @GetMapping()
    public List<Film> findAllFilms() {
        return filmStorage.findAllFilms();
    }

    @GetMapping("/popular")
    public List<Film> findPopular(@RequestParam(defaultValue = "10") Long count) {
        return filmService.findPopularByCount(count);
    }

    @PostMapping()
    public Film create(@RequestBody Film film) throws ValidateException {
        validator.validate(film);
        return filmStorage.create(film);
    }

    @PutMapping()
    public Film update(@RequestBody Film film) throws ValidateException, NotFoundException {
        validator.validate(film);
        return filmStorage.update(film);
    }

    @PutMapping("{id}/like/{userId}")
    public Set<Long> addLike(@PathVariable("id") Long filmId, @PathVariable Long userId) throws NotFoundException {
        return filmService.addLike(filmId, userId);
    }

    @DeleteMapping("{id}/like/{userId}")
    public Set<Long> delLike(@PathVariable("id") Long filmId, @PathVariable("userId") Long userId) throws NotFoundException {
        return filmService.delLike(filmId, userId);
    }


}

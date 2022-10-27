package ru.yandex.practicum.filmorate.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidateException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.film.FilmService;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/films")
public class FilmController {
    private final FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping("/{id}")
    public Film findById(@PathVariable Long id) throws NotFoundException {
        return filmService.getFilmById(id);
    }

    @GetMapping()
    public List<Film> findAllFilms() {
        return filmService.findAllFilms();
    }

    @GetMapping("/popular")
    public List<Film> findPopular(@RequestParam(defaultValue = "10") Long count) {
        return filmService.findPopularByCount(count);
    }

    @PostMapping()
    public Film create(@RequestBody Film film) throws ValidateException, NotFoundException {
        return filmService.create(film);
    }

    @PutMapping()
    public Film update(@RequestBody Film film) throws ValidateException, NotFoundException {
        return filmService.update(film);
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

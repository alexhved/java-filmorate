package ru.yandex.practicum.filmorate.validator;

import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.time.Month;

public class FilmValidator implements Validator<Film> {
    @Override
    public boolean validate(Film film) {
        if (film.getName().isEmpty() || film.getDescription().isEmpty()
                || film.getReleaseDate().isEqual(LocalDate.MAX) || film.getDuration() == 0) {
            return false;
        }

        if (film.getName().isBlank()) {
            return false;
        }

        if (film.getDescription().chars().count() > 200) {
            return false;
        }

        if (film.getReleaseDate().isBefore(LocalDate.of(1895, Month.DECEMBER, 28))) {
            return false;
        }

        if (film.getDuration() < 0) {
            return false;
        }

        if (film.getId() == 0) {
            film.generateId();
        }

        return true;
    }
}

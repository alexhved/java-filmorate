package ru.yandex.practicum.filmorate.service.film;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidateException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.EntityValidator;

import java.time.LocalDate;
import java.time.Month;

@Service
public class FilmValidator implements EntityValidator<Film> {
    @Override
    public void validate(Film film) throws ValidateException {
        if (film.getName()==null || film.getName().isBlank()) {
            throw new ValidateException("The name should not be empty");
        }

        if (film.getDescription()== null
                || film.getDescription().chars().count() > 200 || film.getDescription().isEmpty()) {
            throw new ValidateException("The description should not be empty," +
                    " the maximum length of the description should not exceed 200 characters");
        }

        if (film.getReleaseDate()==null
                || film.getReleaseDate().isBefore(LocalDate.of(1895, Month.DECEMBER, 28))) {
            throw new ValidateException("The release date cannot be earlier than 12/28/1895 and cannot be empty");
        }

        if (film.getDuration() <= 0) {
            throw new ValidateException("The duration of the movie must be greater than 0");
        }

        if (film.getId() == 0) {
            film.setId(FilmService.generateId());
        }
    }
}

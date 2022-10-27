package ru.yandex.practicum.filmorate.service.film;

import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import ru.yandex.practicum.filmorate.exception.ValidateException;
import ru.yandex.practicum.filmorate.id_generator.IdGenerator;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.EntityValidator;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.Month;

@Service
@Validated
public class FilmValidator implements EntityValidator<Film> {
    @Override
    public void validate(@Valid Film film) throws ValidateException {

        if (film.getDescription().chars().count() > 200) {
            throw new ValidateException("The description should not be empty," +
                    " the maximum length of the description should not exceed 200 characters");
        }

        if (film.getReleaseDate().isBefore(LocalDate.of(1895, Month.DECEMBER, 28))) {
            throw new ValidateException("The release date cannot be earlier than 12/28/1895 and cannot be empty");
        }

        if (film.getId() == 0) {
            film.setId(IdGenerator.generateIdForFilm());
        }
    }
}

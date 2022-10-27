package ru.yandex.practicum.filmorate.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidateException;
import ru.yandex.practicum.filmorate.id_generator.IdGenerator;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.film.FilmValidator;

import java.time.LocalDate;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.*;

class FilmValidatorTest {
    EntityValidator<Film> validator = new FilmValidator();
    Film film;

    @BeforeEach
    public void setUp() {
        film = new Film();
        film.setId(IdGenerator.generateIdForFilm());
        film.setName("film name");
        film.setDescription("description");
        film.setDuration(90);
        film.setReleaseDate(LocalDate.now());
    }

    @Test
    void validate() {
        assertDoesNotThrow(() -> validator.validate(film));
    }

    @Test
    void validateDescription() {
        String s = "a".repeat(201);
        film.setDescription(s);
        ValidateException exception = assertThrows(ValidateException.class, () -> validator.validate(film));
        assertEquals("The description should not be empty," +
                " the maximum length of the description should not exceed 200 characters", exception.getMessage());
    }

    @Test
    void validateReleaseDate() {
        LocalDate minReleaseDate = LocalDate.of(1895, Month.DECEMBER, 28);

        film.setReleaseDate(minReleaseDate.minusDays(1));
        ValidateException exception = assertThrows(ValidateException.class, () -> validator.validate(film));
        assertEquals("The release date cannot be earlier than 12/28/1895 and cannot be empty", exception.getMessage());
    }
}
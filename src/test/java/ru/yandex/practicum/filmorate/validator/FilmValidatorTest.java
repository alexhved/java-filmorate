package ru.yandex.practicum.filmorate.validator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.time.Month;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FilmValidatorTest {
    Validator<Film> validator = new FilmValidator();
    Film film;

    @BeforeEach
    public void setUp() {
        film = new Film();
        film.setId(1);
        film.setName("film name");
        film.setDescription("description");
        film.setDuration(90);
        film.setReleaseDate(LocalDate.now());
    }

    @Test
    void validate() {
        assertTrue(validator.validate(film));
    }

    @Test
    void validateWithoutName() {
        film.setName("");
        assertFalse(validator.validate(film));
    }

    @Test
    void validateDescription() {
        String[] s = new String[201];
        for (int i = 0; i < 201; i++) {
            s[i] = "a";
        }
        film.setDescription(Arrays.toString(s));
        assertFalse(validator.validate(film));
    }

    @Test
    void validateReleaseDate() {
        LocalDate minReleaseDate = LocalDate.of(1895, Month.DECEMBER, 28);

        film.setReleaseDate(minReleaseDate.minusDays(1));
        assertFalse(validator.validate(film));

        film.setReleaseDate(minReleaseDate.plusDays(1));
        assertTrue(validator.validate(film));
    }

    @Test
    void validateDuration() {
        film.setDuration(-1);
        assertFalse(validator.validate(film));
    }

    @Test
    void validateEmptyfields() {
        film.setDuration(0);
        film.setId(0);
        film.setReleaseDate(LocalDate.MIN);
        film.setName("");
        film.setDescription("");
        assertFalse(validator.validate(film));
    }
}
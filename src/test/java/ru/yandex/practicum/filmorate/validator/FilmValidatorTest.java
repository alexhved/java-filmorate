package ru.yandex.practicum.filmorate.validator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.exception.ValidateException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.*;

class FilmValidatorTest {
    Validator<Film> validator = new FilmValidator();
    Film film;

    @BeforeEach
    public void setUp() {
        film = new Film();
        film.setId(FilmController.generateId());
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
    void validateWithoutName() {
        film.setName("");
        ValidateException exception = assertThrows(ValidateException.class, () -> validator.validate(film));
        assertEquals("Имя не должно быть пустым", exception.getMessage());
    }

    @Test
    void validateDescription() {
        String s = "a".repeat(201);
        film.setDescription(s);
        ValidateException exception = assertThrows(ValidateException.class, () -> validator.validate(film));
        assertEquals("Описание не должно быть пустым," +
                " максимальная длина описания не должна превышать 200 символов", exception.getMessage());
    }

    @Test
    void validateReleaseDate() {
        LocalDate minReleaseDate = LocalDate.of(1895, Month.DECEMBER, 28);

        film.setReleaseDate(minReleaseDate.minusDays(1));
        ValidateException exception = assertThrows(ValidateException.class, () -> validator.validate(film));
        assertEquals("Дата релиза не может быть ранее 28.12.1895 и не может быть пустым", exception.getMessage());
    }

    @Test
    void validateDuration() {
        film.setDuration(-1);
        ValidateException exception = assertThrows(ValidateException.class, () -> validator.validate(film));
        assertEquals("Продолжительность фильма должна быть больше 0", exception.getMessage());
    }
}
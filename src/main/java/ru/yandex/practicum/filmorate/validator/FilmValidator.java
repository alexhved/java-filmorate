package ru.yandex.practicum.filmorate.validator;

import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.exception.ValidateException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.time.Month;

public class FilmValidator implements Validator<Film> {
    @Override
    public void validate(Film film) throws ValidateException {
        if (film.getName()==null || film.getName().isBlank()) {
            throw new ValidateException("Имя не должно быть пустым");
        }

        if (film.getDescription()== null || film.getDescription().chars().count() > 200 || film.getDescription().isEmpty()) {
            throw new ValidateException("Описание не должно быть пустым," +
                    " максимальная длина описания не должна превышать 200 символов");
        }

        if (film.getReleaseDate()==null || film.getReleaseDate().isBefore(LocalDate.of(1895, Month.DECEMBER, 28))) {
            throw new ValidateException("Дата релиза не может быть ранее 28.12.1895 и не может быть пустым");
        }

        if (film.getDuration() <= 0) {
            throw new ValidateException("Продолжительность фильма должна быть больше 0");
        }

        if (film.getId() == 0) {
            film.setId(FilmController.generateId());
        }
    }
}

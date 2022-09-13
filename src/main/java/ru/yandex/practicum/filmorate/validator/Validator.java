package ru.yandex.practicum.filmorate.validator;

import ru.yandex.practicum.filmorate.exception.ValidateException;

public interface Validator<T> {
    void validate(T t) throws ValidateException;
}

package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.exception.ValidateException;

public interface EntityValidator<T> {
    void validate(T t) throws ValidateException;
}

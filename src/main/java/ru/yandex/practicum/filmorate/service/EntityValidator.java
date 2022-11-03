package ru.yandex.practicum.filmorate.service;

import org.springframework.validation.annotation.Validated;
import ru.yandex.practicum.filmorate.exception.ValidateException;

import javax.validation.Valid;

@Validated
public interface EntityValidator<T> {
    void validate(@Valid T t) throws ValidateException;
}

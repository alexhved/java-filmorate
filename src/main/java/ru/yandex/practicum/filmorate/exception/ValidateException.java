package ru.yandex.practicum.filmorate.exception;

import javax.validation.ValidationException;

public class ValidateException extends ValidationException {
    public ValidateException(String s) {
        super(s);
    }
}

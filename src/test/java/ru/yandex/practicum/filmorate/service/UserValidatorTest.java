package ru.yandex.practicum.filmorate.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidateException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.user.UserService;
import ru.yandex.practicum.filmorate.service.user.UserValidator;

import java.time.LocalDate;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.*;

class UserValidatorTest {
    EntityValidator<User> validator = new UserValidator();
    User user;

    @BeforeEach
    void setUp() {
        user = new User();
        UserService.generateId();
        user.setName("name nick");
        user.setLogin("login");
        user.setEmail("email@.con");
        user.setBirthday(LocalDate.of(2000, Month.JANUARY, 1));
    }

    @Test
    void validate() {
        assertDoesNotThrow(() -> validator.validate(user));

        user.setName("");
        assertDoesNotThrow(() -> validator.validate(user));
    }

    @Test
    void validateMail() {
        user.setEmail("");
        ValidateException ex = assertThrows(ValidateException.class, () -> validator.validate(user));
        assertEquals("The mail must contain the \"@\" character and must not be empty", ex.getMessage());

        user.setEmail("email.ru");
        ValidateException ex2 = assertThrows(ValidateException.class, () -> validator.validate(user));
        assertEquals("The mail must contain the \"@\" character and must not be empty", ex2.getMessage());
    }

    @Test
    void validateLogin() {
        user.setLogin("");
        ValidateException ex = assertThrows(ValidateException.class, () -> validator.validate(user));
        assertEquals("The login cannot be empty or contain a space", ex.getMessage());

        user.setLogin("log in");
        ValidateException ex2 = assertThrows(ValidateException.class, () -> validator.validate(user));
        assertEquals("The login cannot be empty or contain a space", ex2.getMessage());
    }

    @Test
    void validateBirthday() {
        user.setBirthday(LocalDate.MAX);
        ValidateException ex = assertThrows(ValidateException.class, () -> validator.validate(user));
        assertEquals("Birthday can't be in the future", ex.getMessage());
    }
}
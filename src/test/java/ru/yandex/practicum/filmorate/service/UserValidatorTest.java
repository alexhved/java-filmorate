package ru.yandex.practicum.filmorate.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidateException;
import ru.yandex.practicum.filmorate.id_generator.IdGenerator;
import ru.yandex.practicum.filmorate.model.User;
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
        IdGenerator.generateIdForUser();
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
    void validateBirthday() {
        user.setBirthday(LocalDate.MAX);
        ValidateException ex = assertThrows(ValidateException.class, () -> validator.validate(user));
        assertEquals("Birthday can't be in the future", ex.getMessage());
    }
}
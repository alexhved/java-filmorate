package ru.yandex.practicum.filmorate.validator;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.time.Month;

class UserValidatorTest {
    Validator<User> validator = new UserValidator();
    User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.generateId();
        user.setName("name nick");
        user.setLogin("login");
        user.setEmail("email@.con");
        user.setBirthday(LocalDate.of(2000, Month.JANUARY, 1));
    }

    @Test
    void validate() {
        assertTrue(validator.validate(user));

        user.setName("");
        assertTrue(validator.validate(user));
    }

    @Test
    void validateMail() {
        user.setEmail("");
        assertFalse(validator.validate(user));

        user.setEmail("email.ru");
        assertFalse(validator.validate(user));
    }

    @Test
    void validateLogin() {
        user.setLogin("");
        assertFalse(validator.validate(user));

        user.setLogin("log in");
        assertFalse(validator.validate(user));
    }

    @Test
    void validateBirthday() {
        user.setBirthday(LocalDate.MAX);
        assertFalse(validator.validate(user));
    }

    @Test
    void validateEmpty() {
        User emptyUser = new User();
        assertFalse(validator.validate(emptyUser));
    }
}
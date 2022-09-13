package ru.yandex.practicum.filmorate.validator;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exception.ValidateException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.time.Month;

class UserValidatorTest {
    Validator<User> validator = new UserValidator();
    User user;

    @BeforeEach
    void setUp() {
        user = new User();
        UserController.generateId();
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
        assertEquals("Почта должна содержать символ \"@\" и не должна быть пустой", ex.getMessage());

        user.setEmail("email.ru");
        ValidateException ex2 = assertThrows(ValidateException.class, () -> validator.validate(user));
        assertEquals("Почта должна содержать символ \"@\" и не должна быть пустой", ex2.getMessage());
    }

    @Test
    void validateLogin() {
        user.setLogin("");
        ValidateException ex = assertThrows(ValidateException.class, () -> validator.validate(user));
        assertEquals("Логин не может быть пустым или содержать пробел", ex.getMessage());

        user.setLogin("log in");
        ValidateException ex2 = assertThrows(ValidateException.class, () -> validator.validate(user));
        assertEquals("Логин не может быть пустым или содержать пробел", ex2.getMessage());
    }

    @Test
    void validateBirthday() {
        user.setBirthday(LocalDate.MAX);
        ValidateException ex = assertThrows(ValidateException.class, () -> validator.validate(user));
        assertEquals("День рождения не может быть в будущем", ex.getMessage());
    }
}
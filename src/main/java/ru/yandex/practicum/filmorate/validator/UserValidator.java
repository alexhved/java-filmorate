package ru.yandex.practicum.filmorate.validator;

import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exception.ValidateException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

public class UserValidator implements Validator<User> {
    @Override
    public void validate(User user) throws ValidateException {
        if (user.getEmail()==null || user.getEmail().isBlank() || !user.getEmail().contains("@")) {
            throw new ValidateException("Почта должна содержать символ \"@\" и не должна быть пустой");
        }

        if (user.getLogin()==null || user.getLogin().isBlank() || user.getLogin().contains(" ")) {
            throw new ValidateException("Логин не может быть пустым или содержать пробел");
        }

        if (user.getBirthday()==null || user.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidateException("День рождения не может быть в будущем");
        }

        if (user.getName()==null || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }

        if (user.getId() == 0) {
            user.setId(UserController.generateId());
        }
    }
}

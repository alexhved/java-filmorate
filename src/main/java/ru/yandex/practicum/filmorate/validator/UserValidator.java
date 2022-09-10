package ru.yandex.practicum.filmorate.validator;

import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

public class UserValidator implements Validator<User> {
    @Override
    public boolean validate(User user) {
        if (user.getLogin().isEmpty() || user.getEmail().isEmpty() || user.getBirthday().isEqual(LocalDate.MAX)) {
            return false;
        }

        if (user.getEmail().isBlank() || !user.getEmail().contains("@")) {
            return false;
        }

        if (user.getLogin().isBlank() || user.getLogin().contains(" ")) {
            return false;
        }

        if (user.getBirthday().isAfter(LocalDate.now())) {
            return false;
        }

        if (user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }

        if (user.getId() == 0) {
            user.generateId();
        }

        return true;
    }
}

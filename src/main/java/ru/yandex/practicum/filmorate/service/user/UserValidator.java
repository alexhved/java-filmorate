package ru.yandex.practicum.filmorate.service.user;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidateException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.EntityValidator;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import java.time.LocalDate;

@Service
public class UserValidator implements EntityValidator<User> {
    @Override
    public void validate(User user) throws ValidateException {
        if (user.getEmail()==null || user.getEmail().isBlank() || !user.getEmail().contains("@")) {
            throw new ValidateException("The mail must contain the \"@\" character and must not be empty");
        }

        if (user.getLogin()==null || user.getLogin().isBlank() || user.getLogin().contains(" ")) {
            throw new ValidateException("The login cannot be empty or contain a space");
        }

        if (user.getBirthday()==null || user.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidateException("Birthday can't be in the future");
        }

        if (user.getName()==null || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }

        if (user.getId() == 0) {
            user.setId(InMemoryUserStorage.generateId());
        }
    }
}

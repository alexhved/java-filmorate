package ru.yandex.practicum.filmorate.service.user;

import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import ru.yandex.practicum.filmorate.id_generator.IdGenerator;
import ru.yandex.practicum.filmorate.exception.ValidateException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.EntityValidator;

import javax.validation.Valid;
import java.time.LocalDate;

@Service
@Validated
public class UserValidator implements EntityValidator<User> {
    @Override
    public void validate(@Valid User user) throws ValidateException {

        if (user.getBirthday()==null || user.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidateException("Birthday can't be in the future");
        }

        if (user.getName()==null || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }

        if (user.getId() == 0) {
            user.setId(IdGenerator.generateIdForUser());
        }
    }
}

package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import java.time.LocalDate;

@Data
public class User {
    private long id = 0;
    private String email = "";
    private String login = "";
    private String name = "";
    private LocalDate birthday = LocalDate.MAX;
}

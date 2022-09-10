package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import java.time.LocalDate;

@Data
public class User {
    private long id;
    private String email;
    private String login;
    private String name;
    private LocalDate birthday;

    {
        id = 0;
        email = "";
        login = "";
        name = "";
        birthday = LocalDate.MAX;
    }

    public User() {}
    private static long idGenerator = 0;

    public void generateId() {
        id = ++idGenerator;
    }

    public void setName(String name) {
        this.name = name;
    }
}

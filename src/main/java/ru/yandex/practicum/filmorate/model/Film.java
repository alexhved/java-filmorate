package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import java.time.LocalDate;

@Data
public class Film {
    private long id;
    private String name;
    private String description;
    private LocalDate releaseDate;
    private int duration;

    {
        id = 0;
        name = "";
        description = "";
        releaseDate = LocalDate.MAX;
        duration = 0;
    }

    public Film() {}
    private static long idGenerator = 0;
    public void generateId() {
        id = ++idGenerator;
    }
}

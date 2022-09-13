package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import java.time.LocalDate;

@Data
public class Film {
    private long id = 0;
    private String name = "";
    private String description = "";
    private LocalDate releaseDate = LocalDate.MIN;
    private int duration = 0;
}

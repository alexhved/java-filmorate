package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Film {
    private long id;
    @NotEmpty
    private String name;
    @NotEmpty
    private String description;
    @PastOrPresent
    private LocalDate releaseDate;
    @Positive
    private int duration;
    private int rate;
    @NotNull
    private Mpa mpa;

    private final Set<Long> userLikes = new HashSet<>();

    private final Set<Genre> genres = new TreeSet<>(Comparator.comparing(Genre::getId));
}

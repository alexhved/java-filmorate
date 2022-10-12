package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
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
    private final Set<Long> userLikes = new HashSet<>();
}

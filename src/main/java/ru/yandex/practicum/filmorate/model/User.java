package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private long id;
    @Email
    private String email;
    @NotBlank
    @Pattern(regexp = "\\S+")
    private String login;
    private String name;
    @PastOrPresent
    private LocalDate birthday;
    private Set<Long> friends = new HashSet<>();
    private Map<Long, FriendStatus> friendStatus = new HashMap<>();

    public String getStringBirthday() {
        DateTimeFormatter birthDateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return birthday.format(birthDateFormatter);
    }
}

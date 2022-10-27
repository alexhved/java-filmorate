package ru.yandex.practicum.filmorate.id_generator;

public class IdGenerator {
    private static long idGenForUser = 0;
    private static long idGenForFilm = 0;

    public static long generateIdForUser() {
        return ++idGenForUser;
    }
    public static long generateIdForFilm() {
        return ++idGenForFilm;
    }
}

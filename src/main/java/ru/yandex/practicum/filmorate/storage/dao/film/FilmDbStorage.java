package ru.yandex.practicum.filmorate.storage.dao.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.dao.FilmDb;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

@Repository
@Slf4j
public class FilmDbStorage implements FilmDb {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public FilmDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate=jdbcTemplate;
    }

    @Override
    public List<Film> findAllFilms() {
        String sql = "select * from films f, MPA m where f.MPA_ID = m.ID";
        List<Film> films = jdbcTemplate.query(sql, (rs, rowNum) -> Film.builder()
                .id(rs.getLong("id"))
                .name(rs.getString("name"))
                .description(rs.getString("description"))
                .rate(rs.getInt("rate"))
                .releaseDate(rs.getDate("release_date").toLocalDate())
                .duration(rs.getInt("duration"))
                .mpa(new Mpa(rs.getLong(8), rs.getString(9)))
                .build());

        String sqlGenres = "select gf.FILM_ID, gf.GENRE_ID, g.NAME from GENRES_FILMS as gf join GENRES as g on gf.GENRE_ID = g.ID";
        SqlRowSet rsGenres = jdbcTemplate.queryForRowSet(sqlGenres);

        while (rsGenres.next()) {
            for (Film film : films) {
                if (rsGenres.getLong("film_id") == film.getId()) {
                    film.getGenres().add(new Genre(rsGenres.getInt("genre_id"), rsGenres.getString("name")));
                }
            }
        }

        String sqlLikes = "select * from LIKES_USERS";
        SqlRowSet rsLikes = jdbcTemplate.queryForRowSet(sqlLikes);

        while (rsLikes.next()) {
            for (Film film : films) {
                if (rsLikes.getLong("film_id") == film.getId()) {
                    film.getUserLikes().add(rsLikes.getLong("user_id"));
                }
            }
        }

        return films;
    }

    @Override
    public Optional<Film> create(Film film) throws NotFoundException {
        String sql = "insert into FILMS (NAME, DESCRIPTION, RATE, RELEASE_DATE, DURATION, MPA_ID) values (?,?,?,?,?,?)";
        jdbcTemplate.update(sql,
                film.getName(),
                film.getDescription(),
                film.getRate(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId());

        insertGenres(film);

        return getFilmById(film.getId());
    }

    @Override
    public Optional<Film> update(Film film) throws NotFoundException {
        String sql = "update FILMS set NAME = ?, DESCRIPTION = ?, RATE = ?, RELEASE_DATE = ?, DURATION = ?, MPA_ID = ? where ID = ?";
        jdbcTemplate.update(sql,
                film.getName(),
                film.getDescription(),
                film.getRate(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId(),
                film.getId());

        jdbcTemplate.update("delete from GENRES_FILMS where FILM_ID = ?", film.getId());

        insertGenres(film);

        return getFilmById(film.getId());
    }

    @Override
    public Optional<Film> getFilmById(Long filmId) {

        String sql = "select * from FILMS f, MPA m where f.MPA_ID=m.ID and f.ID = ?";
        SqlRowSet rs = jdbcTemplate.queryForRowSet(sql, filmId);
        if (rs.next()) {
            Film film = Film.builder()
                .id(rs.getLong("id"))
                .name(rs.getString("name"))
                .description(rs.getString("description"))
                .rate(rs.getInt("rate"))
                .releaseDate(Objects.requireNonNull(rs.getDate("release_date")).toLocalDate())
                .duration(rs.getInt("duration"))
                .mpa(new Mpa(rs.getLong(8), rs.getString(9)))
                .build();

            film.getGenres().addAll(genresByFilmId(filmId));
            film.getUserLikes().addAll(userLikesByFilmId(filmId));

            log.info("found film: {} {}", film.getId(), film.getName());
            return Optional.of(film);
        } else {
            return Optional.empty();
        }
    }

    @Override
    public void addLike(Long filmId, Long userId) {
        String sql = "insert into LIKES_USERS (FILM_ID, USER_ID) values (?, ?)";
        jdbcTemplate.update(sql, filmId, userId);
    }

    @Override
    public void removeLike(Long filmId, Long userId) {
        String sql = "delete from LIKES_USERS where FILM_ID = ? and USER_ID = ?";
        jdbcTemplate.update(sql, filmId, userId);
    }

    @Override
    public List<Mpa> findAllMpa() {
        String sqlMpa = "select id, name from MPA";
        return jdbcTemplate.query(sqlMpa, (rs, rowNum) ->
                new Mpa(rs.getInt("id"), rs.getString("name")));
    }

    @Override
    public Mpa findMpaById(int id) throws NotFoundException {
        String sqlMpa = "select id, name from MPA where id = ?";
        List<Mpa> mpaList = jdbcTemplate.query(sqlMpa, (rs, rowNum) ->
                new Mpa(rs.getInt("id"), rs.getString("name")), id);
        if (mpaList.size() != 1) {
            throw new NotFoundException(String.format("mpa with id %s not found", id));
        }
        return mpaList.get(0);
    }

    @Override
    public List<Genre> findAllGenres() {
        String sqlGenres = "select id, NAME from GENRES";
        return jdbcTemplate.query(sqlGenres, (rs, rowNum) ->
                new Genre(rs.getInt("id"), rs.getString("name")));
    }

    @Override
    public Genre findGenreById(int id) throws NotFoundException {
        String sqlGenre = "select id, NAME from GENRES where ID = ?";
        List<Genre> genreList = jdbcTemplate.query(sqlGenre, (rs, rowNum) ->
                new Genre(rs.getInt("id"), rs.getString("name")), id);
        if (genreList.size() != 1) {
            throw new NotFoundException(String.format("genre with id %s not found", id));
        }
        return genreList.get(0);
    }

    private List<Genre> genresByFilmId(long filmId) {
        String sqlGenres = "select g.ID, g.NAME from GENRES_FILMS as gf join GENRES as g on gf.GENRE_ID = g.ID where gf.FILM_ID = ?";
        return jdbcTemplate.query(sqlGenres,
                (rs, rowNum) -> new Genre(rs.getInt("id"), rs.getString("name"))
                , filmId);
    }

    private Set<Long> userLikesByFilmId(long filmId) {
        String sqlUserLikes = "select USER_ID from LIKES_USERS where FILM_ID = ?";
        SqlRowSet rs = jdbcTemplate.queryForRowSet(sqlUserLikes, filmId);
        Set<Long> userLikes = new HashSet<>();
        while (rs.next()) {
            userLikes.add(rs.getLong("user_id"));
        }
        return userLikes;
    }

    private void insertGenres(Film film) {
        List<Genre> genres = new ArrayList<>(film.getGenres());
        if (genres.isEmpty()) {
            return;
        }

        jdbcTemplate.batchUpdate("insert into GENRES_FILMS (FILM_ID, GENRE_ID) VALUES (?, ?)",
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        ps.setLong(1, film.getId());
                        ps.setInt(2, genres.get(i).getId());
                    }
                    @Override
                    public int getBatchSize() {
                        return genres.size();
                    }
                });
    }
}

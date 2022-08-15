package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.exception.InvalidValueException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.Validator;
import ru.yandex.practicum.filmorate.storage.user.UserDaoStorage;

import java.sql.*;
import java.sql.Date;
import java.util.*;

@Component
public class FilmDbStorage implements FilmDaoStorage {

    private final JdbcTemplate jdbcTemplate;
    private final UserDaoStorage userDaoStorage;
    private final Validator validator;

    public FilmDbStorage(JdbcTemplate jdbcTemplate, UserDaoStorage userDaoStorage, Validator validator) {
        this.jdbcTemplate = jdbcTemplate;
        this.userDaoStorage = userDaoStorage;
        this.validator = validator;
    }

    @Override
    public Film getFilmById(Long id) {
        if (id < 1) {
            throw new InvalidValueException("Введен некорректный идентификатор фильма.");
        }
        String sql =
                "SELECT F.FILM_ID, F.NAME, F.DESCRIPTION, F.RELEASE_DATE,  " +
                        "F.DURATION, F.RATING_ID, R.RATING_NAME " +
                        "FROM FILMS F " +
                        "JOIN RATINGS AS R ON F.RATING_ID = R.RATING_ID " +
                        "WHERE F.FILM_ID = ?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeFilm(rs), id)
                .stream().findAny().orElse(null);
    }

    @Override
    public List<Film> getAllFilms() {
        String sql =
                "SELECT F.FILM_ID, F.NAME, F.RELEASE_DATE, F.DESCRIPTION,  " +
                        "F.DURATION, F.RATING_ID, R.RATING_NAME " +
                        "FROM FILMS f " +
                        "JOIN RATINGS AS R ON f.RATING_ID = R.RATING_ID " +
                        "ORDER BY F.FILM_ID";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeFilm(rs));
    }

    @Override
    public Film createFilm(Film film) {
        validator.filmValidator(film);
        if (film == null) {
            throw new EntityNotFoundException("Невозможно создать фильм. Передан пустой фильм.");
        }

        String sqlQuery = "INSERT INTO films (NAME, RELEASE_DATE, DESCRIPTION, DURATION, RATING_ID) " +
                "VALUES (?, ?, ?, ?, ?);";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sqlQuery, new String[]{"FILM_ID"});
            ps.setString(1, film.getName());
            ps.setDate(2, Date.valueOf(film.getReleaseDate()));
            ps.setString(3, film.getDescription());
            ps.setLong(4, film.getDuration());
            ps.setLong(5, film.getMpa().getId());
            return ps;
        }, keyHolder);
        film.setId(keyHolder.getKey().longValue());
        return film;
    }

    @Override
    public void createGenreByFilm(Film film) {
        validator.filmValidator(film);
        String sql =
                "INSERT INTO FILMS_GENRES (FILM_ID, GENRE_ID) " +
                        "VALUES(?, ?)";
        Set<Genre> genres = film.getGenres();
        if (genres == null) {
            return;
        }
        for (Genre genre : genres) {
            jdbcTemplate.update(sql, film.getId(), genre.getId());
        }
    }

    @Override
    public Film updateFilm(Film film) {
        validator.filmValidator(film);
        if (!getAllFilms().contains(film)) {
            throw new EntityNotFoundException("Фильм не найден для обновления.");
        }
        if (film.getId() < 1) {
            throw new InvalidValueException("Введен некорректный идентификатор фильма.");
        }
        String sql =
                "UPDATE FILMS " +
                        "SET NAME = ?, RELEASE_DATE = ?, DESCRIPTION = ?, " +
                        "DURATION = ?, RATING_ID =? " +
                        "WHERE FILM_ID = ?";
        jdbcTemplate.update(sql, film.getName(), film.getReleaseDate(), film.getDescription(),
                film.getDuration(), film.getMpa().getId(), film.getId());
        return film;
    }

    @Override
    public void deleteFilm(Film film) {
        validator.filmValidator(film);
        if (!getAllFilms().contains(film)) {
            throw new EntityNotFoundException("Фильм не найден для удаления.");
        }
        if (film.getId() < 1) {
            throw new InvalidValueException("Введен некорректный идентификатор фильма.");
        }
        String sql =
                "DELETE " +
                        "FROM FILMS " +
                        "WHERE FILM_ID = ?";
        jdbcTemplate.update(sql, film.getId());
    }

    @Override
    public List<Film> getTopLikeFilm(Integer count) {
        String sql =
                "SELECT F.FILM_ID, F.NAME, F.RELEASE_DATE, F.DESCRIPTION,  " +
                        "F.DURATION, F.RATING_ID, R.RATING_NAME " +
                        "FROM FILMS F " +
                        "JOIN RATINGS AS R ON f.RATING_ID = R.RATING_ID " +
                        "LEFT JOIN FILMS_LIKES L on F.FILM_ID = L.FILM_ID " +
                        "GROUP BY F.FILM_ID " +
                        "ORDER BY COUNT(L.USER_ID) DESC " +
                        "limit ?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeFilm(rs), count);
    }

    private Film makeFilm(ResultSet rs) throws SQLException {
        Film film = new Film();
        film.setId(rs.getLong("FILM_ID"));
        film.setName(rs.getString("NAME"));
        film.setReleaseDate(rs.getDate("RELEASE_DATE").toLocalDate());
        film.setDescription(rs.getString("DESCRIPTION"));
        film.setDuration(rs.getInt("DURATION"));
        film.setMpa(new Mpa(rs.getInt("RATING_ID"), rs.getString("RATING_NAME")));
        return film;
    }
}
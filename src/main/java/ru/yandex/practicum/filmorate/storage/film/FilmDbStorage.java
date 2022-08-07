package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.*;
import java.time.LocalDate;
import java.util.List;

@Component
public class FilmDbStorage implements FilmDaoStorage {

    private final JdbcTemplate jdbcTemplate;

    public FilmDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Film getFilmById(Long filmId) {
        String sql = "SELECT * FROM FILMS WHERE FILM_ID = ?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeFilm(rs), filmId)
                .stream().findAny().orElse(null);
    }

    @Override
    public List<Film> getAllFilms() {
        String sql = "SELECT * FROM FILMS";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeFilm(rs));
    }

    @Override
    public Film createFilm(Film film) {
        String sql = "SELECT * FROM FILMS WHERE FILM_ID = ?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeFilm(rs), film.getFilmId())
                .stream().findAny().orElse(null);
    }

    @Override
    public Film updateFilm(Film film) {
        if (film.getFilmId() == null) {
            return null;
        }
        String sql = "UPDATE FILMS SET FILM_NAME = ?, DESCRIPTION = ?, DURATION = ?, " +
                "RELEASE_DATE = ?, MPA_ID =? WHERE FILM_ID = ?";
        jdbcTemplate.update(sql, film.getFilmName(), film.getDescription(), film.getReleaseDate(),
                film.getDuration(), film.getMpa());
        return film;
    }

    @Override
    public void deleteFilm(Film film) {
        if (film.getFilmId() == null) {
            return;
        }
        String sql = "DELETE FROM FILMS WHERE FILM_ID = ?";
        jdbcTemplate.update(sql, film.getFilmId());
    }

    @Override
    public Film saveFilm(Film film) {
        String sql = "INSERT INTO FILMS (FILM_NAME, DESCRIPTION, DURATION, RELEASE_DATE, MPA_ID)" +
                " VALUES (?, ?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sql, new String[]{"FILM_ID"});
            stmt.setString(1, film.getFilmName());
            stmt.setString(2, film.getDescription());
            stmt.setLong(3, film.getDuration());
            final LocalDate releaseDate = film.getReleaseDate();
            if (releaseDate == null) {
                stmt.setNull(4, Types.DATE);
            } else {
                stmt.setDate(4, Date.valueOf(releaseDate));
            }
            stmt.setObject(5, film.getMpa()); //уточнить правильность метода
            return stmt;
        }, keyHolder);
        film.setFilmId(keyHolder.getKey().longValue());
        return film;
    }

    private Film makeFilm(ResultSet rs) throws SQLException {
        return new Film(rs.getLong("FILM_ID"), rs.getString("FILM_NAME"),
                rs.getString("DESCRIPTION"), rs.getDate("RELEASE_DATE").toLocalDate(),
                rs.getLong("DURATION"),
/*                rs.getInt("LIKE"),
                new Genre(rs.getInt("GENRE_ID"), rs.getString("GENRE_NAME")),*/
                new Mpa(rs.getInt("MPA_ID"), rs.getString("MPA_NAME")));
    }
}

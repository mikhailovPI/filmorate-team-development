package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class FilmDbStorage implements FilmDaoStorage {

    private final JdbcTemplate jdbcTemplate;

    public FilmDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public Film getFilmById(Long filmId) {
        return null;
    }

    @Override
    public List<Film> getAllFilms() {
        return null;
    }

    @Override
    public Film createFilm(Film film) {
        return null;
    }

    @Override
    public Film updateFilm(Film film) {
        return null;
    }

    @Override
    public void deleteFilm(Film film) {

    }

    @Override
    public Film saveFilm(Film film) {
        return null;
    }

    private Film makeFilm(ResultSet rs) throws SQLException {
        return new Film(rs.getLong("FILM_ID"), rs.getString("FILM_NAME"),
                rs.getString("DESCRIPRION"), rs.getDate("RELEASE_DATE").toLocalDate(),
                rs.getLong("DURATION"),
                new Genre(rs.getInt("GENRE_ID"), rs.getString("GENRE_NAME")),
                new Mpa(rs.getInt("MPA_ID"), rs.getString("MPA_NAME")));
    }
}

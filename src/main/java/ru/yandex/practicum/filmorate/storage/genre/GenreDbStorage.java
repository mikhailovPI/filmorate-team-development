package ru.yandex.practicum.filmorate.storage.genre;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.InvalidValueException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.film.FilmDaoStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class GenreDbStorage implements GenreDaoStorage {

    private final JdbcTemplate jdbcTemplate;

    public GenreDbStorage(JdbcTemplate jdbcTemplate, FilmDaoStorage filmDaoStorage) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Genre getGenreById(Long genreId) {
        if (genreId < 1) {
            throw new InvalidValueException("Введен некорректный идентификатор жанра.");
        }
        String sql =
                "SELECT * " +
                        "FROM GENRES " +
                        "WHERE GENRE_ID = ?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeGenre(rs), genreId)
                .stream().findAny().orElse(null);
    }

    @Override
    public List<Genre> getAllGenres() {
        String sql =
                "SELECT * " +
                        "FROM GENRES " +
                        "ORDER BY GENRE_ID";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeGenre(rs));
    }

    @Override
    public Genre createGenre(Genre genre) {
        String sql =
                "SELECT * " +
                        "FROM GENRES " +
                        "WHERE GENRE_ID = ?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeGenre(rs), genre.getId())
                .stream().findAny().orElse(null);
    }
    @Override
    public Genre updateGenre(Genre genre) {
        if (genre == null) {
            return null;
        }
        if (genre.getId() < 1) {
            throw new InvalidValueException("Введен некорректный идентификатор жанра.");
        }
        String sql =
                "UPDATE GENRES " +
                        "SET NAME = ? " +
                        "WHERE GENRE_ID = ?";
        jdbcTemplate.update(sql, genre.getName(), genre.getId());
        return genre;
    }

    @Override
    public Set<Genre> getGenresByFilm(Film film) {
        String sql =
                "SELECT GEN.GENRE_ID, GEN.NAME " +
                        "FROM GENRES GEN " +
                        "NATURAL JOIN FILMS_GENRES fg " +
                        "WHERE fg.FILM_ID = ?";
        return new HashSet<>(jdbcTemplate.query(sql, (rs, rowNum) -> makeGenre(rs), film.getId()));
    }

    public void updateGenreFilm(Film film) {
        String sql = "DELETE FROM FILMS_GENRES WHERE FILM_ID = ?";
        jdbcTemplate.update(sql, film.getId());
    }

    private Genre makeGenre(ResultSet rs) throws SQLException {
        return new Genre(rs.getInt("GENRE_ID"), rs.getString("NAME"));
    }
}


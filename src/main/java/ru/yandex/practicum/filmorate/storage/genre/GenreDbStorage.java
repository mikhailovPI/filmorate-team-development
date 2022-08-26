package ru.yandex.practicum.filmorate.storage.genre;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static ru.yandex.practicum.filmorate.utilities.Checker.checkFilmExists;
import static ru.yandex.practicum.filmorate.utilities.Checker.checkGenreExists;

@Slf4j
@Component
@RequiredArgsConstructor
public class GenreDbStorage implements GenreDaoStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Genre getGenreById(Integer genreId) {
        checkGenreExists(genreId, jdbcTemplate);
        String sql =
                "SELECT * " +
                        "FROM GENRES " +
                        "WHERE GENRE_ID = ?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeGenre(rs), genreId)
                .stream().findFirst().get();
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
    public Set<Genre> getGenresByFilm(Film film) {
        checkFilmExists(film.getId(), jdbcTemplate);
        String sql =
                "SELECT GEN.GENRE_ID, GEN.NAME " +
                        "FROM GENRES GEN " +
                        "NATURAL JOIN FILMS_GENRES fg " +
                        "WHERE fg.FILM_ID = ?";
        return new HashSet<>(jdbcTemplate.query(sql, (rs, rowNum) -> makeGenre(rs), film.getId()));
    }

    public void updateGenreFilm(Film film) {
        checkFilmExists(film.getId(), jdbcTemplate);
        String sql = "DELETE FROM FILMS_GENRES WHERE FILM_ID = ?";
        jdbcTemplate.update(sql, film.getId());
    }

    private Genre makeGenre(ResultSet rs) throws SQLException {
        return new Genre(rs.getInt("GENRE_ID"), rs.getString("NAME"));
    }
}


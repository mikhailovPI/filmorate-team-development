package ru.yandex.practicum.filmorate.storage.genre;

import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class GenreDbStorage implements GenreDaoStorage {

    private final JdbcTemplate jdbcTemplate;

    public GenreDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Genre getGenreById(Integer genreId) {
        String sql = "SELECT * FROM GENRE WHERE GENRE_ID = ?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeGenre(rs), genreId)
                .stream().findAny().orElse(null);
    }

    @Override
    public List<Genre> getAllGenre() {
        String sql = "SELECT * FROM GENRE";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeGenre(rs));
    }

    @Override
    public Genre createGenre(Genre genre) {
        String sql = "SELECT * FROM GENRE WHERE GENRE_ID = ?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeGenre(rs), genre.getGenreId())
                .stream().findAny().orElse(null);
    }

    @Override
    public Genre updateGenre(Genre genre) {
        if (genre.getGenreId() == null) {
            return null;
        }
        String sql = "UPDATE GENRES SET GENRE_NAME = ? WHERE GENRE_ID = ?";
        jdbcTemplate.update(sql, genre.getGenreName(), genre.getGenreId());
        return genre;
    }

    @Override
    public void deleteGenre(Genre genre) {
        if (genre.getGenreId() == null) {
            return;
        }
        String sql = "DELETE FROM GENRES WHERE GENRE_ID = ?";
        jdbcTemplate.update(sql, genre.getGenreId());
    }

    private Genre makeGenre (ResultSet rs) throws SQLException {
        return new Genre(rs.getInt("GENRE_ID"), rs.getString("GENRE_NAME"));
    }
}


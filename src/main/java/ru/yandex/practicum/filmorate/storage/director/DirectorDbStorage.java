package ru.yandex.practicum.filmorate.storage.director;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.Validator;
import ru.yandex.practicum.filmorate.storage.film.FilmDbStorage;
import ru.yandex.practicum.filmorate.utilities.Checker;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class DirectorDbStorage implements DirectorDaoStorage {

    private final JdbcTemplate jdbcTemplate;
    private final Validator validator;

    private Director makeDirector(ResultSet rs) throws SQLException {
        Director director = new Director();
        director.setId(rs.getInt("DIRECTOR_ID"));
        director.setName(rs.getString("DIRECTOR_NAME"));
        return director;
    }

    @Override
    public List<Director> allDirectors() {
        return jdbcTemplate.query("SELECT * FROM DIRECTORS", (rs, rowNum) -> makeDirector(rs));
    }

    @Override
    public Director findDirectorById(Integer id) {
        Checker.checkDirectorExists(id, jdbcTemplate);
        Director director = jdbcTemplate.query("SELECT * FROM DIRECTORS WHERE DIRECTOR_ID = ?", (rs, rowNum) ->
                        makeDirector(rs), id)
                .stream().findAny().orElse(null);
        if(director == null) {
            throw new EntityNotFoundException("В базе данных режиссер с таким id не найден.");
        }
        return director;
    }

    @Override
    public Director createDirector(Director director) {
        validator.directorValidator(director);
        String sqlQuery = "INSERT INTO DIRECTORS (DIRECTOR_NAME) VALUES (?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"director_id"});
            stmt.setString(1, director.getName());
            return stmt;
        }, keyHolder);
        director.setId(keyHolder.getKey().intValue());
        return findDirectorById(director.getId());
    }

    @Override
    public void deleteDirector(Integer id) {
        Checker.checkDirectorExists(id, jdbcTemplate);
        jdbcTemplate.update("DELETE FROM FILM_DIRECTOR WHERE director_id = ?", id);
        jdbcTemplate.update("DELETE FROM DIRECTORS WHERE director_id = ?", id);
    }

    @Override
    public Set<Director> getDirectorsByFilm(Film film) {
        String sqlQuery = "SELECT DIRECTOR_ID FROM FILM_DIRECTOR WHERE FILM_ID = ?";
        List<Integer> id = jdbcTemplate.queryForList(sqlQuery, Integer.class, film.getId());
        return id.stream().map(this::findDirectorById).collect(Collectors.toSet());
    }

    public void updateDirectorFilm(Film film) {
        String sql = "DELETE FROM FILM_DIRECTOR WHERE FILM_ID = ?";
        jdbcTemplate.update(sql, film.getId());
    }
}

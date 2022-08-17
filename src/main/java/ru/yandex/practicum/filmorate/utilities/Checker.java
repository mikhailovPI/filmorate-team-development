package ru.yandex.practicum.filmorate.utilities;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;

@Slf4j
public class Checker {

    public static void checkUserExists(Long id, JdbcTemplate jdbcTemplate) {
        String sql = "SELECT * FROM USERS WHERE USER_ID =?";
        if (!jdbcTemplate.queryForRowSet(sql, id).next()) {
            log.debug("Пользователь с id: {} не найден.", id);
            throw new EntityNotFoundException((String.format("Пользователь с id: %s не найден.", id)));
        }
    }

    public static void checkFilmExists(Long id, JdbcTemplate jdbcTemplate) {
        String sql = "SELECT * FROM FILMS WHERE FILM_ID =?";
        if (!jdbcTemplate.queryForRowSet(sql, id).next()) {
            log.debug("Фильм с id: {} не найден.", id);
            throw new EntityNotFoundException((String.format("Фильм с id: %s не найден.", id)));
        }
    }

    public static void checkDirectorExists(Integer id, JdbcTemplate jdbcTemplate) {
        String sql = "SELECT * FROM DIRECTORS WHERE DIRECTOR_ID =?";
        if (!jdbcTemplate.queryForRowSet(sql, id).next()) {
            log.debug("Режиссер с id: {} не найден.", id);
            throw new EntityNotFoundException((String.format("Режиссер с id: %s не найден.", id)));
        }
    }
}

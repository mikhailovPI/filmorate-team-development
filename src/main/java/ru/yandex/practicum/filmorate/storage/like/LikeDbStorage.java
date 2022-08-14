package ru.yandex.practicum.filmorate.storage.like;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

@Component
public class LikeDbStorage implements LikeDaoStorage {

    private final JdbcTemplate jdbcTemplate;

    public LikeDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void saveLikes(Long id, Long userId) {
        String sql =
                "merge into FILMS_LIKES(FILM_ID, USER_ID) " +
                "VALUES (?, ?)";

        if (jdbcTemplate.update(sql, id, userId) == 0) {
            throw new EntityNotFoundException(
                    String.format("Ошибка при добавлении в БД LIKES, filmID=%s, userID=%s.", id, userId));
        }
    }

    @Override
    public void removeLikes(Long id, Long userId) {
        jdbcTemplate.update("DELETE FROM FILMS_LIKES WHERE USER_ID = ?", userId);
    }

//    @Override
//    public void loadLikes(Film film) {
//        String sql = "SELECT USER_ID FROM FILMS_LIKES WHERE FILM_ID = ?";
//        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(sql, film.getId());
//        while (sqlRowSet.next()) {
//            film.addLike(sqlRowSet.getLong("USER_ID"));
//        }
//    }
}


package ru.yandex.practicum.filmorate.storage.like;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.film.FilmDaoStorage;
import ru.yandex.practicum.filmorate.storage.user.UserDaoStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Set;

@Component
public class LikeDbStorage implements LikeDaoStorage {

    private final JdbcTemplate jdbcTemplate;
    private final UserDaoStorage userDaoStorage;
    private final FilmDaoStorage filmDaoStorage;

    public LikeDbStorage(JdbcTemplate jdbcTemplate, UserDaoStorage userDaoStorage, FilmDaoStorage filmDaoStorage) {
        this.jdbcTemplate = jdbcTemplate;
        this.userDaoStorage = userDaoStorage;
        this.filmDaoStorage = filmDaoStorage;
    }

    @Override
    public void addLike(Long filmId, Long userId) {
        String sql = "SELECT FILM_ID, USER_ID" +
                "FROM LIKE_USERS" +
                "WHERE FILM_ID = ? AND USER_ID = ?";

        Set<Long> likeSet = filmDaoStorage.getFilmById(filmId).getLike();
        likeSet.add(userId);
    }

    @Override
    public void deleteLike(Long filmId, Long userId) {

    }

    @Override
    public Long getAllLikeFilm(Film film) {
        String sql = "SELECT f.FILM_ID, u.USER_ID" +
                "FROM FILMS F" +
                "LEFT JOIN USERS AS u ON f.FILM_ID = u.USER_ID" +
                "WHERE FILM_ID = ? AND USER_ID = ?" +
                "GROUP BY COUNT(FILM_ID)";
        //List<Long> countLike = jdbcTemplate(sql,);
        //return jdbcTemplate(sql, (rs, rowNum) -> makeLike(rs), film.getId())
        //      .stream().findAny().orElse(null);
        return null;
    }

}

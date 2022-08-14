//package ru.yandex.practicum.filmorate.storage.like;
//
//import org.springframework.jdbc.core.JdbcTemplate;
//import org.springframework.jdbc.support.rowset.SqlRowSet;
//import org.springframework.stereotype.Component;
//import ru.yandex.practicum.filmorate.exception.ValidationException;
//import ru.yandex.practicum.filmorate.model.Film;
//import ru.yandex.practicum.filmorate.model.Mpa;
//import ru.yandex.practicum.filmorate.storage.film.FilmDaoStorage;
//import ru.yandex.practicum.filmorate.storage.user.UserDaoStorage;
//
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.util.List;
//import java.util.Set;
//
//@Component
//public class LikeDbStorage implements LikeDaoStorage {
//
//    private final JdbcTemplate jdbcTemplate;
//    private final UserDaoStorage userDaoStorage;
//    private final FilmDaoStorage filmDaoStorage;
//
//    public LikeDbStorage(JdbcTemplate jdbcTemplate, UserDaoStorage userDaoStorage, FilmDaoStorage filmDaoStorage) {
//        this.jdbcTemplate = jdbcTemplate;
//        this.userDaoStorage = userDaoStorage;
//        this.filmDaoStorage = filmDaoStorage;
//    }
//
//    @Override
//    public void saveLikes(Film film) {
//        jdbcTemplate.update("DELETE FROM FILMS_LIKES WHERE FILM_ID = ?", film.getId());
//
//        String sql = "INSERT INTO FILMS_LIKES (FILM_ID, USER_ID) VALUES(?, ?)";
//        Set<Long> likes = film.getLikes();
//        for (var like : likes ) {
//            jdbcTemplate.update(sql, film.getId(), like);
//        }
//    }
//
//    @Override
//    public void loadLikes(Film film) {
//        String sql = "SELECT USER_ID FROM FILMS_LIKES WHERE FILM_ID = ?";
//        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(sql, film.getId());
//        while (sqlRowSet.next()) {
//            film.setLikes(sqlRowSet.getLong("USER_ID"));
//            //film.addLike(sqlRowSet.getInt("USER_ID"));
//        }
//    }
//}

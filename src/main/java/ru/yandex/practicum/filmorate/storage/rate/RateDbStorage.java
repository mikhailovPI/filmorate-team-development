package ru.yandex.practicum.filmorate.storage.rate;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.enums.EventType;
import ru.yandex.practicum.filmorate.model.enums.OperationType;
import ru.yandex.practicum.filmorate.storage.feed.FeedDaoStorage;

import static ru.yandex.practicum.filmorate.utilities.Checker.checkFilmExists;
import static ru.yandex.practicum.filmorate.utilities.Checker.checkUserExists;

@Slf4j
@Repository
@RequiredArgsConstructor
public class RateDbStorage implements RateDaoStorage {

    private final JdbcTemplate jdbcTemplate;
    private final FeedDaoStorage feedDaoStorage;

    @Override
    public void saveRate(Long filmId, Long userId, Integer rate) {
        checkFilmExists(filmId, jdbcTemplate);
        checkUserExists(userId, jdbcTemplate);

        jdbcTemplate.update("MERGE INTO FILMS_LIKES(FILM_ID, USER_ID, RATE) VALUES (?, ?, ?)",
                filmId,
                userId,
                rate);
        jdbcTemplate.update("UPDATE FILMS SET RATE = ? WHERE FILM_ID = ?", getAverageRate(filmId), filmId);
        feedDaoStorage.addFeed(userId, filmId, EventType.RATE, OperationType.ADD);
    }

    @Override
    public void removeRate(Long filmId, Long userId) {
        checkFilmExists(filmId, jdbcTemplate);
        checkUserExists(userId, jdbcTemplate);

        jdbcTemplate.update("DELETE FROM FILMS_LIKES WHERE USER_ID = ?", userId);
        jdbcTemplate.update("UPDATE FILMS SET RATE = ? WHERE FILM_ID = ?", getAverageRate(filmId), filmId);
        feedDaoStorage.addFeed(userId, filmId, EventType.RATE, OperationType.REMOVE);
    }

    private Double getAverageRate(Long filmId) {
        var sqlQuery = "SELECT AVG(RATE) FROM FILMS_LIKES WHERE FILM_ID = ?";
        return jdbcTemplate.queryForObject(sqlQuery, Double.class, filmId);
    }
}


package ru.yandex.practicum.filmorate.storage.like;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.enums.EventType;
import ru.yandex.practicum.filmorate.storage.feed.FeedDaoStorage;
import ru.yandex.practicum.filmorate.model.enums.OperationType;

import static ru.yandex.practicum.filmorate.utilities.Checker.*;

@Component
@RequiredArgsConstructor
public class LikeDbStorage implements LikeDaoStorage {

    private final JdbcTemplate jdbcTemplate;
    private final FeedDaoStorage feedDaoStorage;

    @Override
    public void saveLikes(Long filmId, Long userId) {
        checkFilmExists(filmId, jdbcTemplate);
        checkUserExists(userId, jdbcTemplate);

        String sql = "MERGE INTO FILMS_LIKES(FILM_ID, USER_ID) VALUES (?, ?)";
        jdbcTemplate.update(sql, filmId, userId);
        feedDaoStorage.addFeed(userId, filmId, EventType.LIKE, OperationType.ADD);
    }

    @Override
    public void removeLikes(Long filmId, Long userId) {
        checkFilmExists(filmId, jdbcTemplate);
        checkUserExists(userId, jdbcTemplate);

        jdbcTemplate.update("DELETE FROM FILMS_LIKES WHERE USER_ID = ?", userId);
        feedDaoStorage.addFeed(userId, filmId, EventType.LIKE, OperationType.REMOVE);
    }
}


package ru.yandex.practicum.filmorate.storage.like;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.enums.EventType;
import ru.yandex.practicum.filmorate.storage.feed.FeedDaoStorage;
import ru.yandex.practicum.filmorate.model.enums.OperationType;

@Component
public class LikeDbStorage implements LikeDaoStorage {

    private final JdbcTemplate jdbcTemplate;
    private final FeedDaoStorage feedDaoStorage;

    public LikeDbStorage(JdbcTemplate jdbcTemplate, FeedDaoStorage feedDaoStorage) {
        this.jdbcTemplate = jdbcTemplate;
        this.feedDaoStorage = feedDaoStorage;
    }

    @Override
    public void saveLikes(Long filmId, Long userId) {
        String sql =
                "merge into FILMS_LIKES(FILM_ID, USER_ID) " +
                        "VALUES (?, ?)";

        if (jdbcTemplate.update(sql, filmId, userId) == 0) {
            throw new EntityNotFoundException(
                    String.format("Ошибка при добавлении в БД LIKES, filmID=%s, userID=%s.", filmId, userId));
        }
        feedDaoStorage.addFeed(userId, filmId, EventType.LIKE, OperationType.ADD);
    }

    @Override
    public void removeLikes(Long filmId, Long userId) {
        jdbcTemplate.update("DELETE FROM FILMS_LIKES WHERE USER_ID = ?", userId);
        feedDaoStorage.addFeed(userId, filmId, EventType.LIKE, OperationType.REMOVE);
    }
}


package ru.yandex.practicum.filmorate.storage.feed;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.Feed;
import ru.yandex.practicum.filmorate.model.enums.EventType;
import ru.yandex.practicum.filmorate.model.enums.OperationType;
import ru.yandex.practicum.filmorate.storage.user.UserDaoStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Objects;

@Repository
public class FeedDbStorage implements FeedDaoStorage {

    private final JdbcTemplate jdbcTemplate;

    private final UserDaoStorage userDaoStorage;

    public FeedDbStorage(JdbcTemplate jdbcTemplate, UserDaoStorage userDaoStorage) {
        this.jdbcTemplate = jdbcTemplate;
        this.userDaoStorage = userDaoStorage;
    }

    @Override
    public void addFeed(long userId, long entityId, EventType eventType, OperationType operationType) {
        String sqlQuery = "INSERT INTO FEED (EVENT_TIMESTAMP, " +
                "USER_ID, " +
                "EVENT_TYPE, " +
                "OPERATION," +
                "ENTITY_ID) VALUES (?, ?, ?, ?, ?)";
        jdbcTemplate.update(sqlQuery,
                Timestamp.valueOf(LocalDateTime.now()),
                userId,
                eventType.toString(),
                operationType.toString(),
                entityId);
    }

    @Override
    public Collection<Feed> getUserFeed(long userId) {
        if (userDaoStorage.getAllUser().stream().noneMatch(u -> Objects.equals(u.getId(), userId))) {
            throw new EntityNotFoundException("Идентификатор пользователя не найден");
        }
        String sql = "SELECT * FROM FEED WHERE USER_ID = ? ORDER BY EVENT_TIMESTAMP";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeFeed(rs), userId);
    }

    private Feed makeFeed(ResultSet rs) throws SQLException {
        Feed feed = new Feed();
        feed.setTimestamp(rs.getTimestamp("EVENT_TIMESTAMP").getTime());
        feed.setUserId(rs.getLong("USER_ID"));
        feed.setEventType(EventType.valueOf(rs.getString("EVENT_TYPE")));
        feed.setOperation(OperationType.valueOf(rs.getString("OPERATION")));
        feed.setEventId(rs.getLong("EVENT_ID"));
        feed.setEntityId(rs.getLong("ENTITY_ID"));
        return feed;
    }
}

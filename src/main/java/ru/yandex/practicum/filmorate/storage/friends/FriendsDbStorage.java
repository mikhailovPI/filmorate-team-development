package ru.yandex.practicum.filmorate.storage.friends;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.model.enums.EventType;
import ru.yandex.practicum.filmorate.storage.feed.FeedDaoStorage;
import ru.yandex.practicum.filmorate.model.enums.OperationType;
import ru.yandex.practicum.filmorate.storage.user.UserDaoStorage;

import java.util.*;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class FriendsDbStorage implements FriendsDaoStorage {

    private final JdbcTemplate jdbcTemplate;
    private final UserDaoStorage userDaoStorage;
    private final FeedDaoStorage feedDaoStorage;

    @Override
    public void addFriend(Long userId, Long friendId) {
        if (userDaoStorage.getAllUser().stream().noneMatch(u -> Objects.equals(u.getId(), userId))) {
            throw new EntityNotFoundException("Идентификатор пользователя не найден");
        } else if (userDaoStorage.getAllUser().stream().noneMatch(u -> Objects.equals(u.getId(), friendId))) {
            throw new EntityNotFoundException("Идентификатор друга не найден");
        }
        String sql =
                "INSERT INTO FRIENDS (USER_ID, FRIEND_ID) " +
                        "VALUES (?, ?)";
        jdbcTemplate.update(sql, userId, friendId);
        feedDaoStorage.addFeed(userId, friendId, EventType.FRIEND, OperationType.ADD);
    }

    @Override
    public void deleteFriend(Long userId, Long friendId) {
        if (userDaoStorage.getAllUser().stream().noneMatch(u -> Objects.equals(u.getId(), userId))) {
            throw new EntityNotFoundException("Идентификатор пользователя не найден");
        } else if (userDaoStorage.getAllUser().stream().noneMatch(u -> Objects.equals(u.getId(), friendId))) {
            throw new EntityNotFoundException("Идентификатор друга не найден");
        }
        String sql =
                "DELETE " +
                        "FROM FRIENDS " +
                        "WHERE USER_ID = ? " +
                        "AND FRIEND_ID = ?";
        jdbcTemplate.update(sql, userId, friendId);
        feedDaoStorage.addFeed(userId, friendId, EventType.FRIEND, OperationType.REMOVE);
    }

    @Override
    public List<User> getAllFriendsUser(Long id) {
        String sql =
                "SELECT FRIEND_ID " +
                        "FROM FRIENDS " +
                        "WHERE USER_ID=?";
        List<Long> friendsUser = jdbcTemplate.queryForList(sql, Long.class, id);
        return friendsUser.stream()
                .map(userDaoStorage::getUserById)
                .collect(Collectors.toList());
    }
}
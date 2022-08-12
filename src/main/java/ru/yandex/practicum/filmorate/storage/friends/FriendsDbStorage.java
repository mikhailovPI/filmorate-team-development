package ru.yandex.practicum.filmorate.storage.friends;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserDaoStorage;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class FriendsDbStorage implements FriendsDaoStorage {

    private final JdbcTemplate jdbcTemplate;
    private final UserDaoStorage userDaoStorage;

    public FriendsDbStorage(JdbcTemplate jdbcTemplate, UserDaoStorage userDaoStorage) {
        this.jdbcTemplate = jdbcTemplate;
        this.userDaoStorage = userDaoStorage;
    }

    @Override
    public void addFriend(Long userId, Long friendId) {
        if (userDaoStorage.getAllUser().stream().noneMatch(u -> Objects.equals(u.getId(), userId))) {
            throw new EntityNotFoundException("Id пользователя не найден");
        } else if (userDaoStorage.getAllUser().stream().noneMatch(u -> Objects.equals(u.getId(), friendId))) {
            throw new EntityNotFoundException("Id друга не найден");
        }
        String sql =
                "INSERT INTO FRIENDS (USER_ID, FRIEND_ID) " +
                        "VALUES (?, ?)";
        jdbcTemplate.update(sql, userId, friendId);
    }

    @Override
    public void deleteFriend(Long userId, Long friendId) {
        if (userDaoStorage.getAllUser().stream().noneMatch(u -> Objects.equals(u.getId(), userId))) {
            throw new EntityNotFoundException("Id пользователя не найден");
        } else if (userDaoStorage.getAllUser().stream().noneMatch(u -> Objects.equals(u.getId(), friendId))) {
            throw new EntityNotFoundException("Id друга не найден");
        }
        String sql =
                "DELETE " +
                        "FROM FRIENDS " +
                        "WHERE USER_ID = ? " +
                        "AND FRIEND_ID = ?";
        jdbcTemplate.update(sql, userId, friendId);
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
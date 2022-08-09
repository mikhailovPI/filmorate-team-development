package ru.yandex.practicum.filmorate.storage.friends;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Friends;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserDaoStorage;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.time.LocalDate;
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

    //список друзей
    @Override
    public List<User> getListFriends(Long userId) {
        String sql = "SELECT FRIEND_ID FROM FRIENDS WHERE USER_ID=?";
        List<Friends> friendsList = jdbcTemplate.query(sql, (rs, rowNum) -> makeFriend(rs), userId);
        Set<User> friendsByUser = friendsList.stream()
                .map(Friends::getFriendsId)
                .map(userDaoStorage::getUserById)
                .collect(Collectors.toSet());
        List<User> allFriendsUser = new ArrayList<>(friendsByUser);
        return allFriendsUser;
    }

    //список общих друзей
    public List<User> getListCommonFriends(Long userId, Long friendId) {
        String sql = "SELECT * FROM FRIENDS WHERE USER_ID = ? AND FRIEND_ID = ?";
        List<Friends> userFriendsList = jdbcTemplate.query(sql, (rs, rowNum) -> makeFriend(rs), userId, friendId);
        Set<User> userSet = userFriendsList.stream()
                .map(Friends::getFriendsId)
                .map(userDaoStorage::getUserById)
                .collect(Collectors.toSet());
        List<User> userFriends = new ArrayList<>(userSet);
        return userFriends;
    }

    //добавление в друзья
    @Override
    public void addFriend(Long userId, Long friendId) {
        String sql = "SELECT * FROM FRIENDS WHERE USER_ID = ? AND FRIEND_ID = ?";
        List<Friends> userFriendsList = jdbcTemplate.query(sql, (rs, rowNum) -> makeFriend(rs), userId, friendId);
    }

    //удаление из друзей
    @Override
    public void deleteFriend(Long userId, Long friendId) {
        if (userDaoStorage.getUserById(friendId) == null) {
            return;
        }
        String sql = "DELETE FROM FRIENDS WHERE USER_ID = ? AND FRIEND_ID = ?";
        jdbcTemplate.update(sql, userId, friendId);
        jdbcTemplate.update(sql, friendId, userId);
    }

    //сохранение в БД - пока не понятно как сохранить
    @Override
    public List<User> saveListFriends(Long userId, Long friendId) {
        return null;
    }

    private Friends makeFriend(ResultSet rs) throws SQLException {
        return new Friends(rs.getLong("USER_ID"), rs.getLong("FRIEND_ID"));
    }
}
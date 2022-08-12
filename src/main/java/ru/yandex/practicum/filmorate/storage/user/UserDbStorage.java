package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.exception.InvalidValueException;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class UserDbStorage implements UserDaoStorage {

    private final JdbcTemplate jdbcTemplate;

    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public User getUserById(Long id) {
        if (id < 1) {
            throw new InvalidValueException("Введен некорректный идентификатор пользователя.");
        }
        String sql =
                "SELECT * " +
                        "FROM USERS " +
                        "WHERE USER_ID = ?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeUser(rs), id)
                .stream()
                .findAny().orElse(null);
    }

    @Override
    public List<User> getAllUser() {
        String sql =
                "SELECT * FROM USERS";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeUser(rs));
    }

    @Override
    public User createUser(User user) {
        if (user == null) {
            throw new EntityNotFoundException("Передан пустой пользователь.");
        }
        KeyHolder keyHolder = new GeneratedKeyHolder();
        String sql =
                "INSERT INTO USERS (EMAIL, LOGIN, USER_NAME, BIRTHDAY) " +
                        "VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sql, new String[]{"USER_ID"});
            stmt.setString(1, user.getEmail());
            stmt.setString(2, user.getLogin());
            stmt.setString(3, user.getName());
            stmt.setDate(4, Date.valueOf(user.getBirthday()));
            return stmt;
        }, keyHolder);
        user.setId(keyHolder.getKey().longValue());
        return user;
    }

    @Override
    public User updateUser(User user) {
        if (getUserById(user.getId()) == null) {
            throw new EntityNotFoundException("Пользователь не найден для обновления.");
        }
        if (user.getId() < 1) {
            throw new InvalidValueException("Введен некорректный идентификатор пользователя.");
        }
        String sql =
                "UPDATE USERS " +
                        "SET EMAIL = ?,  LOGIN = ?, USER_NAME = ?, BIRTHDAY = ?" +
                        " WHERE USER_ID = ?";
        jdbcTemplate.update(sql, user.getEmail(), user.getLogin(), user.getName(), user.getBirthday(),
                user.getId());
        return user;
    }

    @Override
    public void deleteUser(User user) {
        if (getUserById(user.getId()) == null) {
            throw new EntityNotFoundException("Пользователь не найден для удаления.");
        }
        if (user.getId() < 1) {
            throw new InvalidValueException("Введен некорректный идентификатор пользователя.");
        }
        String sql =
                "DELETE FROM USERS " +
                        "WHERE USER_ID = ?";
        jdbcTemplate.update(sql, user.getId());
    }

    @Override
    public void addFriend(Long userId, Long friendId) {
        String sql =
                "INSERT INTO FRIENDS (USER_ID, FRIEND_ID) " +
                        "VALUES (?, ?)";
        jdbcTemplate.update(sql, userId, friendId);
    }

    @Override
    public void deleteFriend(Long userId, Long friendId) {
        if (getUserById(friendId) == null) {
            return;
        }
        String sql =
                "DELETE " +
                        "FROM FRIENDS " +
                        "WHERE USER_ID = ? " +
                        "AND FRIEND_ID = ?";
        jdbcTemplate.update(sql, userId, friendId);
    }

    @Override
    public List<User> getAllFriendsUser(Long userId) {
        String sql =
                "SELECT FRIEND_ID " +
                        "FROM FRIENDS " +
                        "WHERE USER_ID=?";
        List<Long> friendIds = jdbcTemplate.queryForList(sql, Long.class, userId);
        List<User> list = new ArrayList<>();
        for (Long friendId : friendIds) {
            User userById = getUserById(friendId);
            list.add(userById);
        }
        return list;
    }

    private User makeUser(ResultSet rs) throws SQLException {
        User user = new User();
        user.setId(rs.getLong("USER_ID"));
        user.setEmail(rs.getString("EMAIL"));
        user.setLogin(rs.getString("LOGIN"));
        user.setName(rs.getString("USER_NAME"));
        user.setBirthday(rs.getDate("BIRTHDAY").toLocalDate());
        return user;
    }
}
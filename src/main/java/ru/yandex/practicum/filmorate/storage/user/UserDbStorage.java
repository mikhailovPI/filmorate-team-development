package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.*;
import java.time.LocalDate;
import java.util.List;

public class UserDbStorage implements UserDaoStorage {

    private final JdbcTemplate jdbcTemplate;

    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public User getUserById(Long userId) {
        String sql = "SELECT * FROM USERS WHERE USER_ID = ?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeUser(rs), userId)
                .stream().findAny().orElse(null);
    }

    @Override
    public List<User> getAllUser() {
        String sql = "SELECT * FROM USERS";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeUser(rs));
    }

    @Override
    public User createUser(User user) {
        String sql = "SELECT * FROM USERS WHERE USER_ID = ?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeUser(rs), user.getUserId())
                .stream().findAny().orElse(null);
    }

    @Override
    public User updateUser(User user) {
        if (user.getUserId() == null) {
            return null;
        }
        String sql = "UPDATE USERS SET USER_NAME = ?, LOGIN = ?, EMAIL = ?, BIRTHDAY = ? WHERE USER_ID = ?";
        jdbcTemplate.update(sql, user.getUserName(), user.getLogin(), user.getEmail(), user.getBirthday(),
                user.getUserId());
        return user;
    }

    @Override
    public void deleteUser(User user) {
        if (user.getUserId() == null) {
            return;
        }
        String sql = "DELETE FROM USER WHERE USER_ID = ?";
        jdbcTemplate.update(sql, user.getUserId());
    }

    @Override
    public User saveUser(User user) {
        String sql = "INSERT INTO USERS (USER_NAME, LOGIN, EMAIL, BIRTHDAY) VALUES (?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sql, new String[]{"USER_ID"});
            stmt.setString(1, user.getUserName());
            stmt.setString(2, user.getLogin());
            stmt.setString(4, user.getEmail());
            final LocalDate birthday = user.getBirthday();
            if (birthday == null) {
                stmt.setNull(4, Types.DATE);
            } else {
                stmt.setDate(4, Date.valueOf(birthday));
            } return stmt;
        }, keyHolder);
        user.setUserId(keyHolder.getKey().longValue());
        return user;
    }

    private User makeUser (ResultSet rs) throws SQLException {
        return new User(rs.getLong("USER_ID"), rs.getString("USER_NAME"),
                rs.getString("LOGIN"), rs.getString("EMAIL"),
                rs.getDate("BIRTHDAY").toLocalDate());
    }
}
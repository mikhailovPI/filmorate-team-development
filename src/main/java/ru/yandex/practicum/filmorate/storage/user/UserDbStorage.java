package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
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

    private User makeUser (ResultSet rs) throws SQLException {
        return new User(rs.getLong("USER_ID"), rs.getString("USER_NAME"),
                rs.getString("LOGIN"), rs.getString("EMAIL"),
                rs.getDate("BIRTHDAY"));
    }
}
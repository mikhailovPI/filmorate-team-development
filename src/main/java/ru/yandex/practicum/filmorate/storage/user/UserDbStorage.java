package ru.yandex.practicum.filmorate.storage.user;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static ru.yandex.practicum.filmorate.utilities.Checker.*;
import static ru.yandex.practicum.filmorate.utilities.Validator.userValidator;


@Component
@RequiredArgsConstructor
public class UserDbStorage implements UserDaoStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public User getUserById(Long id) {
        checkUserExists(id, jdbcTemplate);
        String sql =
                "SELECT * " +
                        "FROM USERS " +
                        "WHERE USER_ID = ?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeUser(rs), id)
                .stream().findFirst().get();
    }

    @Override
    public List<User> getAllUser() {
        String sql =
                "SELECT * FROM USERS";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeUser(rs));
    }

    @Override
    public User createUser(User user) {
        userValidator(user);
        KeyHolder keyHolder = new GeneratedKeyHolder();
        final String sql = "INSERT INTO USERS(EMAIL, LOGIN, USER_NAME, BIRTHDAY) values (?, ?, ?, ?)";
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
        userValidator(user);
        checkUserExists(user.getId(), jdbcTemplate);
        String sql =
                "UPDATE USERS " +
                        "SET EMAIL = ?,  LOGIN = ?, USER_NAME = ?, BIRTHDAY = ?" +
                        " WHERE USER_ID = ?";
        jdbcTemplate.update(sql, user.getEmail(), user.getLogin(), user.getName(), user.getBirthday(),
                user.getId());
        return user;
    }

    @Override
    public void deleteUser(Long id) {
        checkUserExists(id, jdbcTemplate);
        String sql =
                "DELETE FROM USERS " +
                        "WHERE USER_ID = ?";
        jdbcTemplate.update(sql, id);
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
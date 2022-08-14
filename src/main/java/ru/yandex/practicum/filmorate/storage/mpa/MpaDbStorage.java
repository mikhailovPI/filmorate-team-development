package ru.yandex.practicum.filmorate.storage.mpa;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.exception.InvalidValueException;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
public class MpaDbStorage implements MpaDaoStorage {

    private final JdbcTemplate jdbcTemplate;

    public MpaDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Mpa getMpaById(Integer id) {
        if (id < 1) {
            throw new InvalidValueException("Некорректный идентификатор возрастного ограничения");
        }
        String sql =
                "SELECT * " +
                        "FROM RATINGS " +
                        "WHERE RATING_ID = ?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeMpa(rs), id)
                .stream().findAny().orElse(null);
    }

    @Override
    public List<Mpa> getAllMpa() {
        String sql =
                "SELECT * " +
                        "FROM RATINGS " +
                        "ORDER BY RATING_ID";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeMpa(rs));
    }

    @Override
    public Mpa createMpa(Mpa mpa) {
        String sql =
                "SELECT * " +
                        "FROM RATINGS " +
                        "WHERE RATING_ID = ?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeMpa(rs), mpa.getName())
                .stream().findAny().orElse(null);
    }

    @Override
    public Mpa updateMpa(Mpa mpa) {
        if (getMpaById(mpa.getId()) == null) {
            throw new EntityNotFoundException("MPA не найден для обновления");
        }
        String sql =
                "UPDATE RATINGS " +
                        "SET NAME = ? " +
                        "WHERE RATING_ID = ?";
        jdbcTemplate.update(sql, mpa.getName(), mpa.getId());
        return mpa;
    }

    private Mpa makeMpa(ResultSet rs) throws SQLException {
        return new Mpa(rs.getInt("RATING_ID"), rs.getString("NAME"));
    }
}

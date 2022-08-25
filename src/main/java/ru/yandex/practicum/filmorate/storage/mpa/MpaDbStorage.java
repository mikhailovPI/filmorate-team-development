package ru.yandex.practicum.filmorate.storage.mpa;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Slf4j
@Component
public class MpaDbStorage implements MpaDaoStorage {

    private final JdbcTemplate jdbcTemplate;

    public MpaDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Mpa getMpaById(Integer id) {
        checkMpaExists(id);
        String sql =
                "SELECT * " +
                        "FROM RATINGS " +
                        "WHERE RATING_ID = ?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeMpa(rs), id)
                .stream().findFirst().get();
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
                .stream().findFirst().get();
    }

    @Override
    public Mpa updateMpa(Mpa mpa) {
        checkMpaExists(mpa.getId());
        String sql =
                "UPDATE RATINGS " +
                        "SET RATING_NAME = ? " +
                        "WHERE RATING_ID = ?";
        jdbcTemplate.update(sql, mpa.getName(), mpa.getId());
        return mpa;
    }

    private Mpa makeMpa(ResultSet rs) throws SQLException {
        return new Mpa(rs.getInt("RATING_ID"),
                rs.getString("RATING_NAME"));
    }

    private void checkMpaExists(Integer id) {
        String sql = "SELECT * FROM RATINGS WHERE RATING_ID = ?";
        if (!jdbcTemplate.queryForRowSet(sql, id).next()) {
            log.debug("MPA с id: {} не найден.", id);
            throw new EntityNotFoundException((String.format("MPA с id: %s не найден.", id)));
        }
    }
}

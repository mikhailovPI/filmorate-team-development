package ru.yandex.practicum.filmorate.storage.mpa;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static ru.yandex.practicum.filmorate.utilities.Checker.checkMpaExists;

@Slf4j
@Component
@RequiredArgsConstructor
public class MpaDbStorage implements MpaDaoStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Mpa getMpaById(Integer id) {
        checkMpaExists(id, jdbcTemplate);
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
        checkMpaExists(mpa.getId(), jdbcTemplate);
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
}

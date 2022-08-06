package ru.yandex.practicum.filmorate.storage.mpa;

import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
@Primary
public class MpaDbStorage implements MpaDaoStorage {

    private final JdbcTemplate jdbcTemplate;

    public MpaDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

/*    @Override
    public Mpa getMpaById(Mpa mpa) {
        String sql = "SELECT * FROM MPA WHERE MPA_ID = ?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeMpa(mpa.getMpaId(), rs), mpa.getMpaId())
                .stream().findAny().orElse(null);
    }*/

    @Override
    public Mpa getMpaById(Integer mpaId) {
        //        String sql = "INSERT INTO MPA (MPA_NAME)";
        String sql = "SELECT MP FROM MPA WHERE MPA_ID = ?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeMpa(rs), mpaId)
                .stream().findAny().orElse(null);
    }

    @Override
    public List<Mpa> getAllMpa() {
        String sql = "SELECT * FROM MPA";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeMpa(rs));
    }

    @Override
    public Mpa createMpa(Mpa mpa) {
        String sql = "SELECT * FROM MPA WHERE MPA_ID = ?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeMpa(rs), mpa.getMpaName())
                .stream().findAny().orElse(null);
    }

    @Override
    public Mpa updateMpa(Mpa mpa) {
        if (mpa.getMpaId() == null) {
            return null;
        }
        String sql = "UPDATE MPA SET NAME = ? WHERE MPA_ID = ?";
        jdbcTemplate.update(sql, mpa.getMpaName(), mpa.getMpaId());
        return mpa;
    }

    @Override
    public void deleteMpa(Mpa mpa) {
        if (mpa.getMpaId() == null) {
            return;
        }
        String sql = "DELETE FROM MPA WHERE MPA_ID = ?";
        jdbcTemplate.update(sql, mpa.getMpaId());
    }

    private Mpa makeMpa(ResultSet rs) throws SQLException {
        return new Mpa(rs.getInt("mpa_id"), rs.getString("mpa_name"));
    }
}

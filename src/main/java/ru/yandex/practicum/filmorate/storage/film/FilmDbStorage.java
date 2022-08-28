package ru.yandex.practicum.filmorate.storage.film;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.director.DirectorDaoStorage;
import ru.yandex.practicum.filmorate.storage.genre.GenreDaoStorage;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Set;

import static ru.yandex.practicum.filmorate.utilities.Checker.checkFilmExists;
import static ru.yandex.practicum.filmorate.utilities.Checker.checkUserExists;
import static ru.yandex.practicum.filmorate.utilities.Validator.filmValidator;

@Component
@Slf4j
@RequiredArgsConstructor
public class FilmDbStorage implements FilmDaoStorage {

    private final JdbcTemplate jdbcTemplate;
    private final DirectorDaoStorage directorDaoStorage;
    private final GenreDaoStorage genreDaoStorage;

    @Override
    public Film getFilmById(Long id) {
        checkFilmExists(id, jdbcTemplate);
        String sql =
                "SELECT F.FILM_ID, F.NAME, F.DESCRIPTION, F.RELEASE_DATE,  " +
                        "F.DURATION, F.RATE, F.MPA_ID, R.MPA_NAME " +
                        "FROM FILMS F " +
                        "JOIN MPA AS R ON F.MPA_ID = R.MPA_ID " +
                        "WHERE F.FILM_ID = ?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeFilm(rs), id)
                .stream().findFirst().get();
    }

    @Override
    public List<Film> getAllFilms() {
        String sql =
                "SELECT F.FILM_ID, F.NAME, F.RELEASE_DATE, F.DESCRIPTION,  " +
                        "F.DURATION, F.RATE, F.MPA_ID, R.MPA_NAME " +
                        "FROM FILMS f " +
                        "JOIN MPA AS R ON f.MPA_ID = R.MPA_ID " +
                        "ORDER BY F.FILM_ID";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeFilm(rs));
    }

    @Override
    public Film createFilm(Film film) {
        filmValidator(film);
        film.setRate(0.0);

        String sqlQuery = "INSERT INTO films (NAME, RELEASE_DATE, DESCRIPTION, DURATION, RATE, MPA_ID) " +
                "VALUES (?, ?, ?, ?, ?, ?);";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sqlQuery, new String[]{"FILM_ID"});
            ps.setString(1, film.getName());
            ps.setDate(2, Date.valueOf(film.getReleaseDate()));
            ps.setString(3, film.getDescription());
            ps.setLong(4, film.getDuration());
            ps.setDouble(5, film.getRate());
            ps.setLong(6, film.getMpa().getId());
            return ps;
        }, keyHolder);
        film.setId(keyHolder.getKey().longValue());
        return film;
    }

    @Override
    public void createGenreByFilm(Film film) {
        filmValidator(film);
        String sql =
                "INSERT INTO FILMS_GENRES (FILM_ID, GENRE_ID) " +
                        "VALUES(?, ?)";
        Set<Genre> genres = film.getGenres();
        if (genres == null) {
            return;
        }
        for (Genre genre : genres) {
            jdbcTemplate.update(sql, film.getId(), genre.getId());
        }
    }

    @Override
    public void createDirectorByFilm(Film film) {
        filmValidator(film);
        String sql =
                "INSERT INTO FILM_DIRECTOR (FILM_ID, DIRECTOR_ID) " +
                        "VALUES(?, ?)";
        Set<Director> directors = film.getDirectors();
        if (directors == null) {
            return;
        }
        for (Director director : directors) {
            jdbcTemplate.update(sql, film.getId(), director.getId());
        }
    }

    @Override
    public Film updateFilm(Film film) {
        filmValidator(film);
        checkFilmExists(film.getId(), jdbcTemplate);
        String sql =
                "UPDATE FILMS " +
                        "SET NAME = ?, RELEASE_DATE = ?, DESCRIPTION = ?, " +
                        "DURATION = ?, MPA_ID =? " +
                        "WHERE FILM_ID = ?";
        jdbcTemplate.update(sql, film.getName(), film.getReleaseDate(), film.getDescription(),
                film.getDuration(), film.getMpa().getId(), film.getId());
        return film;
    }

    @Override
    public void deleteFilm(Long id) {
        checkFilmExists(id, jdbcTemplate);
        String sql =
                "DELETE " +
                        "FROM FILMS " +
                        "WHERE FILM_ID = ?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public List<Film> getDirectorsFilmSortByYear(Integer directorId) {
        String sqlQuery =
                "SELECT F.FILM_ID, F.NAME, F.RELEASE_DATE, F.DESCRIPTION, F.DURATION, F.RATE, F.MPA_ID, M.MPA_NAME " +
                        "FROM FILMS AS F " +
                        "JOIN MPA AS M on M.MPA_ID = F.MPA_ID " +
                        "INNER JOIN FILM_DIRECTOR AS FD ON f.FILM_ID = FD.FILM_ID " +
                        "WHERE FD.DIRECTOR_ID = ? " +
                        "ORDER BY F.RELEASE_DATE";
        return getSortedFilmsList(directorId, sqlQuery);
    }

    @Override
    public List<Film> getDirectorsFilmSortByRate(Integer directorId) {
        String sqlQuery = "SELECT F.FILM_ID, F.NAME, F.RELEASE_DATE, F.DESCRIPTION, F.DURATION, F.RATE," +
                "F.MPA_ID, M.MPA_NAME " +
                "FROM FILMS AS F " +
                "JOIN MPA M on M.MPA_ID = F.MPA_ID " +
                "LEFT JOIN FILM_DIRECTOR FD on F.FILM_ID = FD.FILM_ID " +
                "WHERE FD.DIRECTOR_ID = ? " +
                "ORDER BY F.RATE DESC";
        return getSortedFilmsList(directorId, sqlQuery);
    }

    @Override
    public List<Film> getTopLikeFilm(Integer count) {
        String sql =
                "SELECT * FROM FILMS " +
                        "JOIN MPA M on M.MPA_ID = FILMS.MPA_ID " +
                        "ORDER BY RATE DESC " +
                        "LIMIT(?)";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeFilm(rs), count);
    }

    @Override
    public List<Film> getTopFilmsGenreYear(Integer count, Integer genreId, Integer year) {
        String sql =
                "SELECT F.FILM_ID, F.NAME, F.RELEASE_DATE, F.DESCRIPTION, F.DURATION, F.RATE," +
                        " F.MPA_ID, r.MPA_NAME, FG.GENRE_ID " +
                        "FROM FILMS AS F " +
                        "JOIN MPA AS R ON R.MPA_ID = F.MPA_ID " +
                        "LEFT JOIN FILMS_GENRES FG ON F.FILM_ID = FG.FILM_ID " +
                        "WHERE YEAR(F.RELEASE_DATE) = ? AND FG.GENRE_ID = ?  " +
                        "ORDER BY F.RATE DESC " +
                        "LIMIT ?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeFilm(rs), year, genreId, count);
    }

    @Override
    public List<Film> getTopFilmsGenre(Integer count, Integer genreId) {
        String sql =
                "SELECT F.FILM_ID, F.NAME, F.RELEASE_DATE, F.DESCRIPTION, F.DURATION, F.RATE," +
                        "F.MPA_ID, M.MPA_NAME " +
                        "FROM FILMS AS F " +
                        "JOIN MPA AS M ON M.MPA_ID = F.MPA_ID " +
                        "LEFT JOIN FILMS_GENRES FG on F.FILM_ID = FG.FILM_ID " +
                        "WHERE FG.GENRE_ID = ? " +
                        "ORDER BY F.RATE DESC " +
                        "LIMIT ?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeFilm(rs), genreId, count);
    }

    @Override
    public List<Film> getTopFilmsYear(Integer count, Integer year) {
        String sql =
                "SELECT F.FILM_ID, F.NAME, F.RELEASE_DATE, F.DESCRIPTION, F.DURATION, F.RATE," +
                        " F.MPA_ID, M.MPA_NAME " +
                        "FROM FILMS AS F " +
                        "JOIN MPA AS M ON M.MPA_ID = F.MPA_ID " +
                        "WHERE YEAR(F.RELEASE_DATE) = ? " +
                        "ORDER BY F.RATE DESC " +
                        "LIMIT ?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeFilm(rs), year, count);
    }

    public List<Film> getCommonFilms(Long userId, Long friendId) {
        checkUserExists(userId, jdbcTemplate);
        checkUserExists(friendId, jdbcTemplate);
        String sqlQuery = "SELECT DISTINCT F.FILM_ID, NAME, DESCRIPTION, RELEASE_DATE, DURATION, F.RATE, M.MPA_ID, " +
                "MPA_NAME FROM FILMS F " +
                "LEFT JOIN FILMS_LIKES FL ON F.FILM_ID = FL.FILM_ID " +
                "LEFT JOIN MPA M ON F.MPA_ID = M.MPA_ID " +
                "WHERE USER_ID IN (?, ?) " +
                "HAVING COUNT(USER_ID) > 1;";
        return jdbcTemplate.query(sqlQuery, (rs, rowNum) -> makeFilm(rs), userId, friendId);
    }

    @Override
    public List<Film> getSearchFilmsForTitle(String query) {
        String sql =
                "SELECT F.film_id, F.NAME, F.DESCRIPTION, F.RELEASE_DATE, F.DURATION, F.RATE," +
                        "F.MPA_ID, M.MPA_NAME " +
                        "FROM FILMS F " +
                        "LEFT JOIN MPA M ON F.MPA_ID = M.MPA_ID " +
                        "WHERE (f.NAME) ILIKE '%' || (?) || '%' " +
                        "ORDER BY F.RATE DESC";

        List<Film> list = jdbcTemplate.query(sql, (rs, rowNum) -> makeFilm(rs), query);
        log.info("Создан список:" + list);
        return list;
    }

    @Override
    public List<Film> getSearchFilmsForDirector(String query) {
        String sql =
                "SELECT F.FILM_ID, F.NAME, F.DESCRIPTION, F.MPA_ID AS MPA_ID, " +
                        "M.MPA_NAME, F.DURATION, F.RATE, f.RELEASE_DATE " +
                        "FROM FILMS AS F " +
                        "JOIN MPA M ON M.MPA_ID = f.MPA_ID " +
                        "JOIN FILM_DIRECTOR FD ON f.FILM_ID = FD.FILM_ID " +
                        "JOIN DIRECTORS AS D ON D.DIRECTOR_ID = FD.DIRECTOR_ID " +
                        "WHERE D.DIRECTOR_NAME ILIKE '%' || (?) || '%' " +
                        "ORDER BY F.RATE DESC";

        List<Film> list = jdbcTemplate.query(sql, (rs, rowNum) -> makeFilm(rs), query);
        log.info("Создан список:" + list);
        return list;
    }

    @Override
    public List<Film> getSearchFilmsForTitleAndDirector(String query) {
        String sql = "SELECT F.FILM_ID, F.NAME, F.DESCRIPTION, F.MPA_ID AS MPA_ID, " +
                "M.MPA_NAME, F.DURATION, F.RATE, F.RELEASE_DATE " +
                "FROM FILMS AS F " +
                "JOIN MPA AS M ON M.MPA_ID = F.MPA_ID " +
                "LEFT JOIN FILM_DIRECTOR AS FD ON F.FILM_ID = FD.FILM_ID " +
                "LEFT JOIN DIRECTORS D ON D.DIRECTOR_ID = FD.DIRECTOR_ID " +
                "WHERE F.NAME ILIKE '%' || (?) || '%' OR D.DIRECTOR_NAME ILIKE '%' || (?) || '%' " +
                "ORDER BY F.RATE DESC";

        List<Film> list = jdbcTemplate.query(sql, (rs, rowNum) -> makeFilm(rs), query, query);
        log.info("Создан список:" + list);
        return list;
    }

    @Override
    public List<Film> findFilmsLikedByUser(Long id) {
        String queryToFindUserFilms = "SELECT * FROM FILMS " +
                "JOIN MPA M ON M.MPA_ID = FILMS.MPA_ID " +
                "WHERE FILMS.FILM_ID IN (SELECT FILM_ID FROM FILMS_LIKES WHERE (USER_ID = ? AND FILMS.RATE > 5))";
        return jdbcTemplate.query(queryToFindUserFilms, (rs, rowNum) -> makeFilm(rs), id);
    }

    private Film makeFilm(ResultSet rs) throws SQLException {
        Film film = new Film();
        film.setId(rs.getLong("FILM_ID"));
        film.setName(rs.getString("NAME"));
        film.setDescription(rs.getString("DESCRIPTION"));
        film.setReleaseDate(rs.getDate("RELEASE_DATE").toLocalDate());
        film.setDuration(rs.getInt("DURATION"));
        film.setRate(rs.getDouble("RATE"));
        film.setMpa(new Mpa(rs.getInt("MPA_ID"), rs.getString("MPA_NAME")));
        return film;
    }

    private List<Film> getSortedFilmsList(Integer directorId, String sqlQuery) {
        List<Film> films = jdbcTemplate.query(sqlQuery, (rs, rowNum) -> makeFilm(rs), directorId);
        films.forEach(f -> f.setDirectors(directorDaoStorage.getDirectorsByFilm(f)));
        films.forEach(f -> f.setGenres(genreDaoStorage.getGenresByFilm(f)));
        return films;
    }
}
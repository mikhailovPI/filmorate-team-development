package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.exception.InvalidValueException;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.Validator;
import ru.yandex.practicum.filmorate.storage.user.UserDaoStorage;
import ru.yandex.practicum.filmorate.utilities.Checker;
import ru.yandex.practicum.filmorate.storage.director.DirectorDaoStorage;
import ru.yandex.practicum.filmorate.storage.genre.GenreDaoStorage;

import java.sql.*;
import java.sql.Date;
import java.util.*;

@Component
@Slf4j
public class FilmDbStorage implements FilmDaoStorage {

    private final JdbcTemplate jdbcTemplate;
    private final DirectorDaoStorage directorDaoStorage;
    private final GenreDaoStorage genreDaoStorage;
    private final UserDaoStorage userDaoStorage;
    private final Validator validator;

    public FilmDbStorage(JdbcTemplate jdbcTemplate, UserDaoStorage userDaoStorage, Validator validator,
                         GenreDaoStorage genreDaoStorage, DirectorDaoStorage directorDaoStorage) {
        this.jdbcTemplate = jdbcTemplate;
        this.userDaoStorage = userDaoStorage;
        this.validator = validator;
        this.directorDaoStorage = directorDaoStorage;
        this.genreDaoStorage = genreDaoStorage;
    }

    @Override
    public Film getFilmById(Long id) {
        Checker.checkFilmExists(id, jdbcTemplate);
        String sql =
                "SELECT F.FILM_ID, F.NAME, F.DESCRIPTION, F.RELEASE_DATE,  " +
                        "F.DURATION, F.RATING_ID, R.RATING_NAME " +
                        "FROM FILMS F " +
                        "JOIN RATINGS AS R ON F.RATING_ID = R.RATING_ID " +
                        "WHERE F.FILM_ID = ?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeFilm(rs), id)
                .stream().findAny().orElse(null);
    }

    @Override
    public List<Film> getAllFilms() {
        String sql =
                "SELECT F.FILM_ID, F.NAME, F.RELEASE_DATE, F.DESCRIPTION,  " +
                        "F.DURATION, F.RATING_ID, R.RATING_NAME " +
                        "FROM FILMS f " +
                        "JOIN RATINGS AS R ON f.RATING_ID = R.RATING_ID " +
                        "ORDER BY F.FILM_ID";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeFilm(rs));
    }

    @Override
    public Film createFilm(Film film) {
        validator.filmValidator(film);
        if (film == null) {
            throw new EntityNotFoundException("Невозможно создать фильм. Передан пустой фильм.");
        }

        String sqlQuery = "INSERT INTO films (NAME, RELEASE_DATE, DESCRIPTION, DURATION, RATING_ID) " +
                "VALUES (?, ?, ?, ?, ?);";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sqlQuery, new String[]{"FILM_ID"});
            ps.setString(1, film.getName());
            ps.setDate(2, Date.valueOf(film.getReleaseDate()));
            ps.setString(3, film.getDescription());
            ps.setLong(4, film.getDuration());
            ps.setLong(5, film.getMpa().getId());
            return ps;
        }, keyHolder);
        film.setId(keyHolder.getKey().longValue());
        return film;
    }

    @Override
    public void createGenreByFilm(Film film) {
        validator.filmValidator(film);
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
        validator.filmValidator(film);
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
        validator.filmValidator(film);
        if (!getAllFilms().contains(film)) {
            throw new EntityNotFoundException("Фильм не найден для обновления.");
        }
        if (film.getId() < 1) {
            throw new InvalidValueException("Введен некорректный идентификатор фильма.");
        }
        String sql =
                "UPDATE FILMS " +
                        "SET NAME = ?, RELEASE_DATE = ?, DESCRIPTION = ?, " +
                        "DURATION = ?, RATING_ID =? " +
                        "WHERE FILM_ID = ?";
        jdbcTemplate.update(sql, film.getName(), film.getReleaseDate(), film.getDescription(),
                film.getDuration(), film.getMpa().getId(), film.getId());
        return film;
    }

    @Override
    public void deleteFilm(Long id) {
        Checker.checkFilmExists(id, jdbcTemplate);
        String sql =
                "DELETE " +
                        "FROM FILMS " +
                        "WHERE FILM_ID = ?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public List<Film> getDirectorsFilmSortByYear(Integer directorId) {
        String sqlQuery =
                "SELECT f.FILM_ID, f.NAME, f.RELEASE_DATE, f.DESCRIPTION, f.DURATION, f.RATING_ID, r.RATING_NAME " +
                        "FROM FILMS AS f " +
                        "JOIN RATINGS r on r.RATING_ID = f.RATING_ID " +
                        "INNER JOIN FILM_DIRECTOR AS fd on f.FILM_ID = fd.FILM_ID " +
                        "WHERE fd.DIRECTOR_ID = ? " +
                        "ORDER BY f.RELEASE_DATE";
        List<Film> films = jdbcTemplate.query(sqlQuery, (rs, rowNum) -> makeFilm(rs), directorId);
        films.forEach(f -> f.setDirectors(directorDaoStorage.getDirectorsByFilm(f)));
        films.forEach(f -> f.setGenres(genreDaoStorage.getGenresByFilm(f)));
        return films;
    }

    @Override
    public List<Film> getDirectorsFilmSortByLikesCount(Integer directorId) {
        String sqlQuery = "SELECT f.FILM_ID, f.NAME, f.RELEASE_DATE, f.DESCRIPTION, f.DURATION, " +
                " f.RATING_ID, r.RATING_NAME " +
                "FROM FILMS as f " +
                "JOIN RATINGS r on r.RATING_ID = f.RATING_ID " +
                "LEFT JOIN FILMS_LIKES fl on f.film_id = fl.film_id " +
                "LEFT JOIN FILM_DIRECTOR fd on f.FILM_ID = fd.FILM_ID " +
                "WHERE fd.DIRECTOR_ID = ? " +
                "GROUP BY f.FILM_ID " +
                "ORDER BY COUNT(fl.USER_ID) DESC";
        List<Film> films = jdbcTemplate.query(sqlQuery, (rs, rowNum) -> makeFilm(rs), directorId);
        films.forEach(f -> f.setDirectors(directorDaoStorage.getDirectorsByFilm(f)));
        films.forEach(f -> f.setGenres(genreDaoStorage.getGenresByFilm(f)));
        return films;
    }

    @Override

=======
    public List<Film> getTopLikeFilm(Integer count) {
        String sql =
                "SELECT F.FILM_ID, F.NAME, F.RELEASE_DATE, F.DESCRIPTION,  " +
                        "F.DURATION, F.RATING_ID, R.RATING_NAME " +
                        "FROM FILMS F " +
                        "JOIN RATINGS AS R ON f.RATING_ID = R.RATING_ID " +
                        "LEFT JOIN FILMS_LIKES L on F.FILM_ID = L.FILM_ID " +
                        "GROUP BY F.FILM_ID " +
                        "ORDER BY COUNT(L.USER_ID) DESC " +
                        "limit ?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeFilm(rs), count);
    }

    @Override
    public List<Film> getTopFilmsGenreYear(Integer count, Integer genreId, Integer year) {
        String sql =
                "SELECT f.FILM_ID, f.NAME, f.RELEASE_DATE, f.DESCRIPTION, f.DURATION, " +
                        " f.RATING_ID, r.RATING_NAME, GEN.GENRE_ID " +
                        "FROM FILMS f " +
                        "JOIN RATINGS AS r ON r.RATING_ID = f.RATING_ID " +
                        "LEFT JOIN FILMS_LIKES l on f.FILM_ID = l.FILM_ID " +
                        "LEFT JOIN FILMS_GENRES fg on f.FILM_ID = fg.FILM_ID " +
                        "JOIN GENRES GEN on FG.GENRE_ID = GEN.GENRE_ID " +
                        "WHERE YEAR(F.RELEASE_DATE) = ? AND GEN.GENRE_ID = ?  " +
                        "GROUP BY F.FILM_ID " +
                        "ORDER BY COUNT(l.USER_ID) DESC " +
                        "LIMIT ?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeFilm(rs), year, genreId,  count);
    }

    @Override
    public List<Film> getTopFilmsGenre(Integer count, Integer genreId) {
        String sql =
                "SELECT f.FILM_ID, f.NAME, f.RELEASE_DATE, f.DESCRIPTION, f.DURATION, " +
                        " f.RATING_ID, r.RATING_NAME " +
                        "FROM FILMS f " +
                        "JOIN RATINGS AS r ON r.RATING_ID = f.RATING_ID " +
                        "LEFT JOIN FILMS_LIKES l on f.FILM_ID = l.FILM_ID " +
                        "LEFT JOIN FILMS_GENRES fg on f.FILM_ID = fg.FILM_ID " +
                        "JOIN GENRES GEN on FG.GENRE_ID = GEN.GENRE_ID " +
                        "WHERE GEN.GENRE_ID = ? " +
                        "GROUP BY F.FILM_ID " +
                        "ORDER BY COUNT(l.USER_ID) DESC " +
                        "LIMIT ?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeFilm(rs), genreId, count);
    }

    @Override
    public List<Film> getTopFilmsYear(Integer count, Integer year) {
        String sql =
                "SELECT f.FILM_ID, f.NAME, f.RELEASE_DATE, f.DESCRIPTION, f.DURATION, " +
                        " f.RATING_ID, r.RATING_NAME " +
                        "FROM FILMS f " +
                        "JOIN RATINGS AS r ON r.RATING_ID = f.RATING_ID " +
                        "LEFT JOIN FILMS_LIKES l on f.FILM_ID = l.FILM_ID " +
                        "LEFT JOIN FILMS_GENRES fg on f.FILM_ID = fg.FILM_ID " +
                        "JOIN GENRES GEN on FG.GENRE_ID = GEN.GENRE_ID " +
                        "WHERE YEAR(F.RELEASE_DATE) = ? " +
                        "GROUP BY F.FILM_ID " +
                        "ORDER BY COUNT(l.USER_ID) DESC " +
                        "LIMIT ?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeFilm(rs), year, count);

    public List<Film> getCommonFilms(Long userId, Long friendId) {
        Checker.checkUserExists(userId, jdbcTemplate);
        Checker.checkUserExists(friendId, jdbcTemplate);
        String sqlQuery = "SELECT distinct F.film_id, name, description, release_date, duration, R.rating_id, " +
                "RATING_NAME, director_id FROM FILMS F " +
                "LEFT JOIN FILMS_LIKES FL on F.FILM_ID = FL.FILM_ID " +
                "LEFT JOIN RATINGS R on F.RATING_ID = R.RATING_ID " +
                "WHERE USER_ID IN (?, ?) " +
                "HAVING COUNT(USER_ID) > 1;";
        return jdbcTemplate.query(sqlQuery, (rs, rowNum) -> makeFilm(rs), userId, friendId);
    }

    private Film makeFilm(ResultSet rs) throws SQLException {
        Film film = new Film();
        film.setId(rs.getLong("FILM_ID"));
        film.setName(rs.getString("NAME"));
        film.setReleaseDate(rs.getDate("RELEASE_DATE").toLocalDate());
        film.setDescription(rs.getString("DESCRIPTION"));
        film.setDuration(rs.getInt("DURATION"));
        film.setMpa(new Mpa(rs.getInt("RATING_ID"), rs.getString("RATING_NAME")));
        return film;
    }
}
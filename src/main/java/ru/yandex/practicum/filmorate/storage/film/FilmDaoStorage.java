
package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.data.relational.core.sql.In;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmDaoStorage {

    Film getFilmById(Long id);

    List<Film> getAllFilms();

    Film createFilm(Film film);

    Film updateFilm(Film film);

    void deleteFilm(Film film);

    void createGenreByFilm(Film film);

    //void updateGenre(Film film);

    Integer putLike (Long filmId, Long userId);
}


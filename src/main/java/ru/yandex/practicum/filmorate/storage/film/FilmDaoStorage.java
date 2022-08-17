
package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmDaoStorage {

    Film getFilmById(Long id);

    List<Film> getAllFilms();

    Film createFilm(Film film);

    Film updateFilm(Film film);

    void deleteFilm(Long id);

    void createGenreByFilm(Film film);

    List<Film> getTopLikeFilm(Integer count);

    void createDirectorByFilm(Film film);

    List<Film> getDirectorsFilmSortByYear(Integer directorId);

    List<Film> getDirectorsFilmSortByLikesCount(Integer directorId);

    List<Film> getTopFilmsGenreYear(Integer limit, Integer genreId, Integer year);

    List<Film> getTopFilmsGenre(Integer limit, Integer genreId);

    List<Film> getTopFilmsYear(Integer limit, Integer year);
}


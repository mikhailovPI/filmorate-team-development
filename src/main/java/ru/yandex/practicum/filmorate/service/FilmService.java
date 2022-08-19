package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.director.DirectorDaoStorage;
import ru.yandex.practicum.filmorate.storage.film.FilmDaoStorage;
import ru.yandex.practicum.filmorate.storage.genre.GenreDaoStorage;
import ru.yandex.practicum.filmorate.storage.like.LikeDaoStorage;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class FilmService {

    private final FilmDaoStorage filmDaoStorage;
    private final LikeDaoStorage likeDaoStorage;
    private final GenreDaoStorage genreDaoStorage;
    private final DirectorDaoStorage directorDaoStorage;

    private void loadData(Film film) {
        film.setGenres(genreDaoStorage.getGenresByFilm(film));
        film.setDirectors(directorDaoStorage.getDirectorsByFilm(film));
    }

    public List<Film> getFilms() {
        List<Film> films = filmDaoStorage.getAllFilms();
        for (Film film : films) {
            loadData(film);
        }
        return films;
    }

    public Film getFilmById(Long filmId) {
        Film film = filmDaoStorage.getFilmById(filmId);
        loadData(film);
        return film;
    }

    public Film createFilm(Film film) throws ValidationException {
        Film createFilm = filmDaoStorage.createFilm(film);
        filmDaoStorage.createGenreByFilm(createFilm);
        filmDaoStorage.createDirectorByFilm(createFilm);
        return createFilm;
    }

    public Film updateFilm(Film film) throws ValidationException {
        genreDaoStorage.updateGenreFilm(film);
        filmDaoStorage.createGenreByFilm(film);
        directorDaoStorage.updateDirectorFilm(film);
        filmDaoStorage.createDirectorByFilm(film);
        return filmDaoStorage.updateFilm(film);
    }

    public void removeFilm(Long id) throws ValidationException {
        filmDaoStorage.deleteFilm(id);
    }

    public void putLike(Long id, Long userId) {
        likeDaoStorage.saveLikes(id, userId);
    }

    public void removeLike(Long id, Long userId) {
        likeDaoStorage.removeLikes(id, userId);
    }

    public List<Film> getTopFilmsGenreYear(Integer count, Integer genreId, Integer year) {
        if (genreId == null && year == null) {
            List<Film> topFilms = new ArrayList<>();
            for (Film film : filmDaoStorage.getTopLikeFilm(count)) {
                loadData(film);
                topFilms.add(film);
            }
            return topFilms;
        } else if (year == null) {
            List<Film> topFilmsWithGenre = new ArrayList<>();
            for (Film film : filmDaoStorage.getTopFilmsGenre(count, genreId)) {
                loadData(film);
                topFilmsWithGenre.add(film);
            }
            return topFilmsWithGenre;
        } else if (genreId == null) {
            List<Film> topFilmsWithYear = new ArrayList<>();
            for (Film film : filmDaoStorage.getTopFilmsYear(count, year)) {
                loadData(film);
                topFilmsWithYear.add(film);
            }
            return topFilmsWithYear;
        } else {
            List<Film> topFilmsWithYearAndGenre = new ArrayList<>();
            for (Film film : filmDaoStorage.getTopFilmsGenreYear(count, genreId, year)) {
                loadData(film);
                topFilmsWithYearAndGenre.add(film);
            }
            return topFilmsWithYearAndGenre;
        }
    }

    public List<Film> getCommonFilms(Long userId, Long friendId) {
        return filmDaoStorage.getCommonFilms(userId, friendId);
    }

    public List<Film> getSearchFilms(String query, String by) {
        if (by.equals("title")) {
            List<Film> searchFilms = new ArrayList<>();
            for (Film film : filmDaoStorage.getSearchFilmsForTitle(query)) {
                loadData(film);
                searchFilms.add(film);
            }
            return searchFilms;
        } else if (by.equals("director")) {
            List<Film> searchFilms = new ArrayList<>();
            for (Film film : filmDaoStorage.getSearchFilmsForDirector(query)) {
                loadData(film);
                searchFilms.add(film);
            }
            return searchFilms;
        } else {
            List<Film> searchFilms = new ArrayList<>();
            for (Film film : filmDaoStorage.getSearchFilmsForTitleAndDirector(query)) {
                loadData(film);
                searchFilms.add(film);
            }
            return searchFilms;
        }
    }
}

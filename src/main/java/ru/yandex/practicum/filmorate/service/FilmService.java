package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmDaoStorage;
import ru.yandex.practicum.filmorate.storage.genre.GenreDaoStorage;
import ru.yandex.practicum.filmorate.storage.like.LikeDaoStorage;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FilmService {

    private final FilmDaoStorage filmDaoStorage;
    private final LikeDaoStorage likeDaoStorage;
    private final GenreDaoStorage genreDaoStorage;

    private void loadData(Film film) {
        film.setGenres(genreDaoStorage.getGenresByFilm(film));
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
        return createFilm;
    }

    public Film updateFilm(Film film) throws ValidationException {
        genreDaoStorage.updateGenreFilm(film);
        filmDaoStorage.createGenreByFilm(film);
        return filmDaoStorage.updateFilm(film);
    }

    public void removeFilm(Film film) throws ValidationException {
        filmDaoStorage.deleteFilm(film);
    }

    public void putLike(Long id, Long userId) {
        likeDaoStorage.saveLikes(id, userId);
    }

    public void removeLike(Long id, Long userId) {
        likeDaoStorage.removeLikes(id, userId);
    }

    public List<Film> getTopLikeFilm(Integer count) {
        return filmDaoStorage.getTopLikeFilm(count);
    }
}

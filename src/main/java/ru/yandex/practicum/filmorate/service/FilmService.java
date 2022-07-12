
package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.List;
@Service
public class FilmService {

    private final FilmStorage filmStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    public List<Film> get() {
        return filmStorage.getFilms();
    }

    public Film create(Film film) throws ValidationException {
        return filmStorage.createFilm(film);
    }

    public Film update(Film film) throws ValidationException {
        return filmStorage.updateFilm(film);
    }

    public Film delete(Film film) throws ValidationException {
        return filmStorage.deleteFilm(film);
    }
}


package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.List;
import java.util.stream.Collectors;

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

    public void delete(Film film) throws ValidationException {
        filmStorage.deleteFilm(film);
    }
    public long putLike (Film film, User user) {
        film.getLike().add(user.getId());
        return film.getLike().size();
    }

    public void removeLike (Film film, User user) {
        film.getLike().remove(user.getId());
    }

    public List<Film> getTopLikeFilm(Integer count) {
        return filmStorage.getFilms().stream().sorted((p0, p1) ->
                p1.getLike().size() - (p0.getLike().size())).
                limit(count).collect(Collectors.toList());
    }
}

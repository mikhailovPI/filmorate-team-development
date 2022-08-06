package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmDaoStorage;
import ru.yandex.practicum.filmorate.storage.user.UserDaoStorage;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FilmService {

    private final FilmDaoStorage filmDaoStorage;
    private final UserDaoStorage userDaoStorage;

    @Autowired
    public FilmService(FilmDaoStorage filmDaoStorage, UserDaoStorage userDaoStorage) {
        this.filmDaoStorage = filmDaoStorage;
        this.userDaoStorage = userDaoStorage;
    }

    public List<Film> getFilm() {
        return filmDaoStorage.getFilms();
    }

    public Film getFilmById(Long filmId) {
        return filmDaoStorage.getFilmById(filmId);
    }

    public Film createFilm(Film film) throws ValidationException {
        if (film == null) {
            throw new EntityNotFoundException("Передан пустой фильм.");
        }
        return filmDaoStorage.createFilm(film);
    }

    public Film updateFilm(Film film) throws ValidationException {
        if (filmDaoStorage.getFilmById(film.getFilmId()) == null) {
            throw new EntityNotFoundException("Фильм не найден для обновления.");
        }
        return filmDaoStorage.updateFilm(film);
    }

    public void removeFilm(Film film) throws ValidationException {
        if (filmDaoStorage.getFilmById(film.getFilmId()) == null) {
            throw new EntityNotFoundException("Фильм не найден для удаления.");
        }
        filmDaoStorage.deleteFilm(film);
    }

    public Integer putLike(Long idFilm, Long idUser) {
        if (filmDaoStorage.getFilmById(idFilm).getLike().contains(idUser)) {
            throw new ValidationException("Пользователь " + userDaoStorage.getUserById(idUser) +
                    " уже оценивал этот фильм.");
        }
        filmDaoStorage.getFilmById(idFilm).getLike().add(idUser);
        return filmDaoStorage.getFilmById(idFilm).getLike().size();
    }

    public Integer removeLike(Long idFilm, Long idUser) {
        if (!filmDaoStorage.getFilmById(idFilm).getLike().contains(idUser)) {
            throw new ValidationException("Пользователь " + userDaoStorage.getUserById(idUser) +
                    " не оценивал этот фильм.");
        }
        filmDaoStorage.getFilmById(idFilm).getLike().remove(idUser);

        return filmDaoStorage.getFilmById(idFilm).getLike().size();
    }

    public List<Film> getTopLikeFilm(Integer count) {
        return filmDaoStorage.getFilms().stream().sorted((p0, p1) ->
                        p1.getLike().size() - (p0.getLike().size())).
                limit(count).collect(Collectors.toList());
    }
}

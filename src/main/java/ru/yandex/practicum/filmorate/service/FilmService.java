package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmDaoStorage;
import ru.yandex.practicum.filmorate.storage.genre.GenreDaoStorage;
import ru.yandex.practicum.filmorate.storage.mpa.MpaDaoStorage;
import ru.yandex.practicum.filmorate.storage.user.UserDaoStorage;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FilmService {

    private final FilmDaoStorage filmDaoStorage;
    private final UserDaoStorage userDaoStorage;
    private final MpaDaoStorage mpaDaoStorage;
    private final GenreDaoStorage genreDaoStorage;

    public List<Film> getFilms() {
        return filmDaoStorage.getAllFilms();
    }

    public Film getFilmById(Long filmId) {
        return filmDaoStorage.getFilmById(filmId);
    }

    public Film createFilm(Film film) throws ValidationException {
        Film createFilm = filmDaoStorage.createFilm(film);
        filmDaoStorage.createGenreByFilm(createFilm);
        return createFilm;
    }

    public Film updateFilm(Film film) throws ValidationException {
        genreDaoStorage.updateGenreFilm(film);
        filmDaoStorage.createGenreByFilm(film);
        filmDaoStorage.updateFilm(film);
        return film;
    }

    public void removeFilm(Film film) throws ValidationException {
        filmDaoStorage.deleteFilm(film);
    }

    public Integer putLike(Long filmId, Long userId) {
/*        if (filmDaoStorage.getFilmById(filmId).getLike().contains(userId)) {
            throw new ValidationException("Пользователь " + userDaoStorage.getUserById(userId) +
                    " уже оценивал этот фильм.");
        }
        filmDaoStorage.getFilmById(filmId).getLike().add(userId);*/
        return filmDaoStorage.putLike(filmId, userId);
    }

    public Integer removeLike(Long idFilm, Long idUser) {
//        if (!filmDaoStorage.getFilmById(idFilm).getLike().contains(idUser)) {
//            throw new ValidationException("Пользователь " + userDaoStorage.getUserById(idUser) +
//                    " не оценивал этот фильм.");
//        }
        filmDaoStorage.getFilmById(idFilm).getLike().remove(idUser);
        return filmDaoStorage.getFilmById(idFilm).getLike().size();
    }

    public List<Film> getTopLikeFilm(Integer count) {
        return filmDaoStorage.getAllFilms().stream().sorted((p0, p1) ->
                        p1.getLike().size() - (p0.getLike().size())).
                limit(count).collect(Collectors.toList());
    }

//    public List<Genre> getAllGenres() {
//        return genreDaoStorage.getAllGenres();
//    }
//
//    public Genre getGenreById(Long id) {
//        return genreDaoStorage.getGenreById(id);
//    }
//
//    public List<Mpa> getAllMpa() {
//        return mpaDaoStorage.getAllMpa();
//    }
//
//    public Mpa getMpaById(Integer id) {
//        return mpaDaoStorage.getMpaById(id);
//    }
}

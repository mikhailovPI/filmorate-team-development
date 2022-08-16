package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.director.DirectorDaoStorage;
import ru.yandex.practicum.filmorate.storage.film.FilmDaoStorage;
import ru.yandex.practicum.filmorate.storage.genre.GenreDaoStorage;
import ru.yandex.practicum.filmorate.storage.like.LikeDaoStorage;

import java.util.ArrayList;
import java.util.List;

@Service
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

    public List<Film> getTopLikeFilm(Integer count) {
        List<Film> topFilmsWithGenre = new ArrayList<>();
        for (Film film : filmDaoStorage.getTopLikeFilm(count)) {
            loadData(film);
            topFilmsWithGenre.add(film);
        }
        return topFilmsWithGenre;
    }

    public List<Film> getDirectorsFilm(Integer directorId, String sortBy) {
        getDirectorById(directorId);
        if(sortBy.equals("year")) {
           return filmDaoStorage.getDirectorsFilmSortByYear(directorId);
        }
        if(sortBy.equals("likes")) {
           return filmDaoStorage.getDirectorsFilmSortByLikesCount(directorId);
        }
        throw new EntityNotFoundException("Некорректные входные данные");
    }

    public List<Director> getDirectors(){
        return directorDaoStorage.allDirectors();
    }

    public Director getDirectorById(Integer id) {
        Director director = directorDaoStorage.findDirectorById(id);
        if(director == null) {
            throw new EntityNotFoundException("Некорректный id");
        }
        return director;
    }

    public Director createDirector(Director director) {
        return directorDaoStorage.createDirector(director);
    }

    public Director updateDirector(Director director) {
        return directorDaoStorage.updateDirector(director);
    }

    public void deleteDirector(Integer id) {
        directorDaoStorage.deleteDirector(id);
    }
}

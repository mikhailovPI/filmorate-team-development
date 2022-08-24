package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.InvalidValueException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.director.DirectorDaoStorage;
import ru.yandex.practicum.filmorate.storage.film.FilmDaoStorage;
import ru.yandex.practicum.filmorate.storage.genre.GenreDaoStorage;
import ru.yandex.practicum.filmorate.storage.like.LikeDaoStorage;
import ru.yandex.practicum.filmorate.storage.user.UserDaoStorage;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class FilmService {

    private final FilmDaoStorage filmDaoStorage;
    private final LikeDaoStorage likeDaoStorage;
    private final GenreDaoStorage genreDaoStorage;
    private final DirectorDaoStorage directorDaoStorage;
    private final UserDaoStorage userDaoStorage;

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
        List<Film> topFilms = new ArrayList<>();
        if (genreId == null && year == null) {
            for (Film film : filmDaoStorage.getTopLikeFilm(count)) {
                loadData(film);
                topFilms.add(film);
            }
            return topFilms;
        } else if (year == null) {
            for (Film film : filmDaoStorage.getTopFilmsGenre(count, genreId)) {
                loadData(film);
                topFilms.add(film);
            }
            return topFilms;
        } else if (genreId == null) {
            for (Film film : filmDaoStorage.getTopFilmsYear(count, year)) {
                loadData(film);
                topFilms.add(film);
            }
            return topFilms;
        } else {
            for (Film film : filmDaoStorage.getTopFilmsGenreYear(count, genreId, year)) {
                loadData(film);
                topFilms.add(film);
            }
            return topFilms;
        }
    }

    public List<Film> getCommonFilms(Long userId, Long friendId) {
        return filmDaoStorage.getCommonFilms(userId, friendId);
    }

    public List<Film> findRecommendedFilms(Long id) {
        HashMap<User, List<Film>> filmsTable = new HashMap<>();
        List<Film> userFilms = filmDaoStorage.findFilmsLikedByUser(id);
        List<User> users = userDaoStorage.getAllUser();

        users.remove(userDaoStorage.getUserById(id));

        for (User other : users) {
            List<Film> otherFilms = filmDaoStorage.findFilmsLikedByUser(other.getId());
            filmsTable.put(other, otherFilms);
        }

        List<List<Film>> differencesTable = new ArrayList<>();

        for (List<Film> value : filmsTable.values()) {
            List<Film> filmsPackage = new ArrayList<>();

            for (Film film : value) {
                film = getFilmById(film.getId());
                if (!userFilms.contains(film)) {
                    filmsPackage.add(film);
                }
            }
            differencesTable.add(filmsPackage);
        }

        differencesTable.removeIf(List::isEmpty);

        return differencesTable.stream()
                .min(Comparator.comparing(List<Film>::size))
                .orElse(new ArrayList<>());
    }

    public List<Film> getSearchFilms(String query, String by) {
        List<Film> searchFilms = new ArrayList<>();
        if (by.equals("title")) {
            for (Film film : filmDaoStorage.getSearchFilmsForTitle(query)) {
                loadData(film);
                searchFilms.add(film);
            }
            return searchFilms;
        } else if (by.equals("director")) {
            for (Film film : filmDaoStorage.getSearchFilmsForDirector(query)) {
                loadData(film);
                searchFilms.add(film);
            }
            return searchFilms;
        } else if (by.equals("director,title") || by.equals("title,director")) {
            for (Film film : filmDaoStorage.getSearchFilmsForTitleAndDirector(query)) {
                loadData(film);
                searchFilms.add(film);
            }
            return searchFilms;
        }
        else {
            throw new InvalidValueException("Некорректные входные данные");
        }
    }
}

package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;

@RestController
@Slf4j
@Validated
public class FilmController {

    private final FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping("/films")
    public List<Film> getFilms() {
        return filmService.getFilms();
    }

    @GetMapping("/films/{filmId}")

    public Film getFilmById(@PathVariable @Min(1) Long filmId) {
        return filmService.getFilmById(filmId);
    }

    @PostMapping(value = "/films")
    public Film createFilm(@Valid @RequestBody Film film) throws EntityNotFoundException, ValidationException {
        return filmService.createFilm(film);
    }

    @PutMapping(value = "/films")
    public Film updateFilm(@Valid @RequestBody Film film) {
        return filmService.updateFilm(film);
    }

    @DeleteMapping(value = "/films/{id}")
    public void removeFilm(@Valid @RequestBody @PathVariable Long id) {
        filmService.removeFilm(id);
    }

    @PutMapping(value = "/films/{id}/like/{userId}")
    public void putLike(
            @PathVariable @Min(1) Long id,
            @PathVariable @Min(1) Long userId) {
        filmService.putLike(id, userId);
    }

    @DeleteMapping(value = "/films/{id}/like/{userId}")
    public void removeLike(
            @PathVariable @Min(1) Long id,
            @PathVariable @Min(1) Long userId) {
        filmService.removeLike(id, userId);
    }

    @GetMapping(value = "/films/popular")
    public List<Film> getTopFilmsGenreYear(
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) Integer genreId,
            @RequestParam(defaultValue = "10", required = false) Integer count) {

        return filmService.getTopFilmsGenreYear(count, genreId, year);
    }

    @GetMapping(value = "/films/common")
    public List<Film> getCommonFilms(@RequestParam Long userId,
                                     @RequestParam Long friendId) {
        return filmService.getCommonFilms(userId, friendId);
    }

    @GetMapping(value = "/films/search")
    public List<Film> getSearchFilms(
            @RequestParam String query,
            @RequestParam(required = false) String by) {
        return filmService.getSearchFilms(query, by);
    }
}
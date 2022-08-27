package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.DirectorService;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.util.List;


@Slf4j
@Validated
@RestController
@RequestMapping("/films")
public class FilmController {

    private final FilmService filmService;
    private final DirectorService directorService;

    @Autowired
    public FilmController(FilmService filmService, DirectorService directorService) {
        this.filmService = filmService;
        this.directorService = directorService;
    }

    @GetMapping()
    public List<Film> getFilms() {
        return filmService.getFilms();
    }

    @GetMapping("/{filmId}")

    public Film getFilmById(@PathVariable Long filmId) {
        return filmService.getFilmById(filmId);
    }

    @PostMapping()
    public Film createFilm(@Valid @RequestBody Film film) throws EntityNotFoundException, ValidationException {
        return filmService.createFilm(film);
    }

    @PutMapping()
    public Film updateFilm(@Valid @RequestBody Film film) {
        return filmService.updateFilm(film);
    }

    @DeleteMapping(value = "/{id}")
    public void removeFilm(@Valid @RequestBody @PathVariable Long id) {
        filmService.removeFilm(id);
    }

    @PutMapping(value = "/{id}/like/{userId}")
    public void putLike(
            @PathVariable Long id,
            @PathVariable Long userId) {
        filmService.putLike(id, userId);
    }

    @DeleteMapping(value = "/{id}/like/{userId}")
    public void removeLike(
            @PathVariable Long id,
            @PathVariable Long userId) {
        filmService.removeLike(id, userId);
    }

    @GetMapping(value = "/popular")
    public List<Film> getTopFilmsGenreYear(
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) Integer genreId,
            @RequestParam(defaultValue = "10", required = false) Integer count) {

        return filmService.getTopFilmsGenreYear(count, genreId, year);
    }

    @GetMapping(value = "/common")
    public List<Film> getCommonFilms(@RequestParam Long userId,
                                     @RequestParam Long friendId) {
        return filmService.getCommonFilms(userId, friendId);
    }

    @GetMapping(value = "/search")
    public List<Film> getSearchFilms(
            @RequestParam String query,
            @RequestParam(required = false) String by) {
        return filmService.getSearchFilms(query, by);
    }

    @GetMapping(value = "/director/{directorId}")
    public List<Film> getDirectorsFilms(
            @PathVariable Integer directorId,
            @RequestParam String sortBy) {
        log.info("Все фильмы режиссера");
        return directorService.getDirectorsFilm(directorId, sortBy);
    }
}
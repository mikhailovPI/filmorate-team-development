
package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.*;

@RestController
@Slf4j
public class FilmController {

    private final FilmService filmService;
    private final UserService userService;

    @Autowired
    public FilmController(FilmService filmService, UserService userService) {
        this.filmService = filmService;
        this.userService = userService;
    }

    @GetMapping("/films")
    public List<Film> getFilm() {
        return filmService.getFilm();
    }

    @GetMapping("/films/{filmId}")
    public Film getFilmId(@PathVariable(value = "filmId") Long filmId) {
        return filmService.getFilmId(filmId);
    }

    @PostMapping(value = "/films")
    public Film createFilm(@Valid @RequestBody Film film) {
        return filmService.createFilm(film);
    }

    @PutMapping(value = "/films")
    public Film updateFilm(@Valid @RequestBody Film film) {
        return filmService.updateFilm(film);
    }

    @DeleteMapping(value = "/films")
    public void removeFilm(@Valid @RequestBody Film film) {
        filmService.removeFilm(film);
    }

    @PutMapping(value = "/films/{id}/like/{userId}")
    public Integer putLikeFilm(
            @PathVariable("id") Long filmId,
            @PathVariable("userId") Long userId) {
        return filmService.putLike(filmService.getFilmId(filmId).getId(), userId);
    }

    @DeleteMapping(value = "/films/{id}/like/{userId}")
    public Integer removeLike(
            @PathVariable("id") Long filmId,
            @PathVariable("userId") Long userId) {
        return filmService.removeLike(filmService.getFilmId(filmId).getId(), userId);
    }

    @GetMapping(value = "/films/popular")
    public List<Film> getTopLikeFilm(
            @RequestParam(defaultValue = "10", required = false) Integer count) {
        return filmService.getTopLikeFilm(count);
    }
}
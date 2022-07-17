package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
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
    public List<Film> getFilm() {
        return filmService.getFilm();
    }

    @GetMapping("/films/{filmId}")
    public Film getFilmById(@PathVariable @Min(1) Long filmId) {
        return filmService.getFilmById(filmId);
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
            @PathVariable @Min(1) Long id,
            @PathVariable @Min(1) Long userId) {
        return filmService.putLike(id, userId);
    }

    @DeleteMapping(value = "/films/{id}/like/{userId}")
    public Integer removeLike(
            @PathVariable @Min(1) Long id,
            @PathVariable @Min(1) Long userId) {
        return filmService.removeLike(filmService.getFilmById(id).getId(), userId);
    }

    @GetMapping(value = "/films/popular")
    public List<Film> getTopLikeFilm(
            @RequestParam(defaultValue = "10", required = false) Integer count) {
        return filmService.getTopLikeFilm(count);
    }
}
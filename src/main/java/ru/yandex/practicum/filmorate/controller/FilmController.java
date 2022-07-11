package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.*;

@RestController
@Slf4j
public class FilmController {

    private InMemoryFilmStorage filmStorage;

    public FilmController(InMemoryFilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    @GetMapping("/films")
    public List<Film> get() {
        return filmStorage.getFilms();
    }

    @PostMapping(value = "/films")
    public Film create(@Valid @RequestBody Film film) throws ValidationException {
        return filmStorage.createFilm(film);
    }

    @PutMapping(value = "/films")
    public Film update(@Valid @RequestBody Film film) throws ValidationException {
        return filmStorage.updateFilm(film);
    }

    @DeleteMapping(value = "/films")
    public Film delete(@Valid @RequestBody Film film) throws ValidationException {
        return filmStorage.deleteFilm(film);
    }
}
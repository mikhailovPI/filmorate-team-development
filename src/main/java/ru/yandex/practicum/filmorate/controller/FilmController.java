package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.*;

@RestController
@Slf4j
public class FilmController {

    private long id = 0;
    private static final LocalDate DATE_RELEASE = LocalDate.of(1895, 12, 28);
    private Map<Long, Film> films = new HashMap<>();

    @GetMapping("/films")
    public List<Film> get() {
        return new ArrayList<>(films.values());
    }

    @PostMapping(value = "/films")
    public Film creat(@Valid @RequestBody Film film) throws ValidationException {
        if (films.containsKey(film.getId())) {
            log.error("Фильм: " + film.getName() + " уже существует.");
            throw new ValidationException("Фильм: " + film.getName() + " уже существует.");
        } else if (film.getReleaseDate().isBefore(DATE_RELEASE)) {
            log.error("Дата релиза фильма не может быть раньше " + DATE_RELEASE);
            throw new ValidationException("Дата релиза фильма не может быть раньше " + DATE_RELEASE);
        }
        film.setId(++id);
        films.put(film.getId(), film);
        log.info("Создали фильм: " + film.toString() + ".");
        return films.get(film.getId());
    }

    @PutMapping(value = "/films")
    public Film update(@Valid @RequestBody Film film) throws ValidationException {
        if (films.containsKey(film.getId())) {
            films.put(film.getId(), film);
            log.info("Обновили фильм: " + film.toString() + ".");
        } else {
            log.error("Название фильма не может быть пустым.");
            throw new ValidationException("Название фильма не может быть пустым.");
        }
        return films.get(film.getId());
    }
}
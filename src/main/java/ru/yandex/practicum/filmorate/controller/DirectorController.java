package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.util.List;

@RestController
@Slf4j
@Validated
@RequiredArgsConstructor
public class DirectorController {
    private final FilmService filmService;

    @GetMapping(value = "/films/director/{directorId}")
    public List<Film> getDirectorsFilms(
            @PathVariable Integer directorId,
            @RequestParam String sortBy) {
        log.info("Все фильмы режиссера");
        return filmService.getDirectorsFilm(directorId, sortBy);
    }

    @GetMapping(value = "/directors")
    public List<Director> getDirectors() {
        log.info("Все режиссеры");
        return filmService.getDirectors();
    }

    @GetMapping(value = "/directors/{id}")
    public Director getDirectorById (
            @PathVariable Integer id) {
        log.info("Получение режиссера по id");
        return filmService.getDirectorById(id);
    }

    @PostMapping(value = "/directors")
    public Director createDirector(
            @Valid @RequestBody Director directors) {
        log.info("Создание режиссера");
        return filmService.createDirector(directors);
    }

    @PutMapping(value = "/directors")
    public Director updateDirector(
            @Valid @RequestBody Director directors) {
        log.info("Обновление режиссера");
        return filmService.updateDirector(directors);
    }

    @DeleteMapping(value = "/directors/{id}")
    public void deleteDirector(
            @PathVariable Integer id) {
        log.info("Удаление режиссера");
        filmService.deleteDirector(id);
    }
}

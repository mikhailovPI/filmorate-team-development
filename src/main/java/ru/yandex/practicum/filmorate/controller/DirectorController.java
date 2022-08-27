package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.service.DirectorService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/directors")
@Slf4j
@Validated
@RequiredArgsConstructor
public class DirectorController {
    private final DirectorService directorService;

    @GetMapping()
    public List<Director> getDirectors() {
        log.info("Все режиссеры");
        return directorService.getDirectors();
    }

    @GetMapping(value = "/{id}")
    public Director getDirectorById(
            @PathVariable Integer id) {
        log.info("Получение режиссера по id");
        return directorService.getDirectorById(id);
    }

    @PostMapping()
    public Director createDirector(
            @Valid @RequestBody Director directors) {
        log.info("Создание режиссера");
        return directorService.createDirector(directors);
    }

    @PutMapping()
    public Director updateDirector(
            @Valid @RequestBody Director directors) {
        log.info("Обновление режиссера");
        return directorService.updateDirector(directors);
    }

    @DeleteMapping(value = "/{id}")
    public void deleteDirector(
            @PathVariable Integer id) {
        log.info("Удаление режиссера");
        directorService.deleteDirector(id);
    }
}

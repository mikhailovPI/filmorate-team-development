package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.genre.GenreDaoStorage;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/genres")
public class GenreController {
    private final GenreDaoStorage genreDaoStorage;

    @Autowired
    public GenreController(GenreDaoStorage genreDaoStorage) {
        this.genreDaoStorage = genreDaoStorage;
    }

    @GetMapping
    public List<Genre> getGenres() {
        return genreDaoStorage.getAllGenres();
    }

    @GetMapping("{id}")
    public Genre getGenre(@PathVariable Long id) {
        return genreDaoStorage.getGenreById(id);
    }
}
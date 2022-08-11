package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.mpa.MpaDaoStorage;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/mpa")
public class MpaController {

    private final MpaDaoStorage mpaDaoStorage;

    @Autowired
    public MpaController(MpaDaoStorage mpaDaoStorage) {
        this.mpaDaoStorage = mpaDaoStorage;
    }

    @GetMapping("/{id}")
    public Mpa findById(@PathVariable Integer id) {
        return mpaDaoStorage.getMpaById(id);
    }

    @GetMapping
    public List<Mpa> findAll() {
        return mpaDaoStorage.getAllMpa();
    }

    @PostMapping
    public Mpa create(@Valid @RequestBody Mpa mpa) {
        return mpaDaoStorage.createMpa(mpa);
    }

    @PutMapping
    public Mpa update(@Valid @RequestBody Mpa mpa) {
        return mpaDaoStorage.updateMpa(mpa);
    }
}
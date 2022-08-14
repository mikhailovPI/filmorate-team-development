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
public class MpaController {

    private final MpaDaoStorage mpaDaoStorage;

    @Autowired
    public MpaController(MpaDaoStorage mpaDaoStorage) {
        this.mpaDaoStorage = mpaDaoStorage;
    }

    @GetMapping("/mpa/{id}")
    public Mpa findById(@PathVariable Integer id) {
        return mpaDaoStorage.getMpaById(id);
    }

    @GetMapping("/mpa")
    public List<Mpa> findAll() {
        return mpaDaoStorage.getAllMpa();
    }

    @PostMapping("/mpa")
    public Mpa create(@Valid @RequestBody Mpa mpa) {
        return mpaDaoStorage.createMpa(mpa);
    }

    @PutMapping("/mpa")
    public Mpa update(@Valid @RequestBody Mpa mpa) {
        return mpaDaoStorage.updateMpa(mpa);
    }
}
package ru.yandex.practicum.filmorate.storage.mpa;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

public interface MpaDaoStorage {

    Mpa getMpaById(Integer mpaId);

    List<Mpa> getAllMpa();

    Mpa createMpa(Mpa mpa);

    Mpa updateMpa(Mpa mpa);

    //void deleteMpa(Mpa mpa);

    //Mpa getMpa(Film film);
}

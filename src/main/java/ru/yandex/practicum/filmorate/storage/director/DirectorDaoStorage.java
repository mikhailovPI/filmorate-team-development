package ru.yandex.practicum.filmorate.storage.director;

import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Set;

public interface DirectorDaoStorage {

    List<Director> allDirectors();

    Director findDirectorById(Integer id);

    Director createDirector(Director director);

    void deleteDirector(Integer id);

    Set<Director> getDirectorsByFilm(Film film);

    void updateDirectorFilm(Film film);
}

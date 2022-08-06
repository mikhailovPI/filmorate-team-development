package ru.yandex.practicum.filmorate.storage.genre;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

public interface GenreDaoStorage {

    Genre getGenreById(Integer genreId);

    List<Genre> getAllGenre();

    Genre createGenre(Genre genre);

    Genre updateGenre(Genre genre);

    void deleteGenre(Genre genre);
}

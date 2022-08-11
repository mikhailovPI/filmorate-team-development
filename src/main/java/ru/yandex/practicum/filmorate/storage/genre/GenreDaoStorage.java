package ru.yandex.practicum.filmorate.storage.genre;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;
import java.util.Set;

public interface GenreDaoStorage {

    Genre getGenreById(Long genreId);

    List<Genre> getAllGenres();

    Genre createGenre(Genre genre);

    Genre updateGenre(Genre genre);

    Set<Genre> getGenresByFilm (Film film);

    void deleteGenre(Genre genre);

    void updateGenreFilm (Film film);
}

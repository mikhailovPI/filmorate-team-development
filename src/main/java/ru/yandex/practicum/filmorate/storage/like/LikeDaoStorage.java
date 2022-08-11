package ru.yandex.practicum.filmorate.storage.like;

import ru.yandex.practicum.filmorate.model.Film;

import java.io.File;

public interface LikeDaoStorage {

    void addLike(Long filmId, Long userId);
    void deleteLike(Long filmId, Long userId);

    Long getAllLikeFilm(Film film);



}

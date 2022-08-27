package ru.yandex.practicum.filmorate.storage.rate;

public interface RateDaoStorage {

    void saveRate(Long filmId, Long userId, Integer rate);

    void removeRate(Long filmId, Long userId);
}

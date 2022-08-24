package ru.yandex.practicum.filmorate.storage.like;

public interface LikeDaoStorage {

    void saveLikes(Long id, Long userId);

    void removeLikes(Long id, Long userId);
}

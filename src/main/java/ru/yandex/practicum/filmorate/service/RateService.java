package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.storage.rate.RateDaoStorage;

@Service
@Slf4j
@RequiredArgsConstructor
public class RateService {

    private final RateDaoStorage rateDaoStorage;

    public void saveRate(Long filmId, Long userId, Integer rate) {
        rateDaoStorage.saveRate(filmId, userId, rate);
    }

    public void removeRate(Long filmId, Long userId) {
        rateDaoStorage.removeRate(filmId, userId);
    }

}

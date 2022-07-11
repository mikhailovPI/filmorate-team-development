package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {

    private long id = 0;
    private static final LocalDate DATE_RELEASE = LocalDate.of(1895, 12, 28);
    private Map<Long, Film> films = new HashMap<>();

    @Override
    public Film createFilm(Film film) {
        if (films.containsKey(film.getId())) {
            throw new ValidationException("Фильм: " + film.getName() + " уже существует.");
        } else if (film.getReleaseDate().isBefore(DATE_RELEASE)) {
            throw new ValidationException("Дата релиза фильма не может быть раньше " + DATE_RELEASE);
        } else {
            film.setId(++id);
            films.put(film.getId(), film);
            log.info("Создали фильм: " + film.toString() + ".");
            return film;
        }
    }

    @Override
    public Film updateFilm(Film film) {
        if (films.containsKey(film.getId())) {
            films.put(film.getId(), film);
            log.info("Обновили фильм: " + film.toString() + ".");
            return film;
        } else {
            log.error("Id фильма не найден.");
            throw new ValidationException("Id фильма не найден.");
        }
    }

    @Override
    public Film deleteFilm(Film film) {
        if (films.containsKey(film.getId())) {
            throw new ValidationException("Фильм: " + film.getName() + " уже существует.");
        } else {
            log.info("Удалили фильм: " + film.toString() + ".");
            films.remove(film.getId());
            return null;
        }

    }

    @Override
    public List<Film> getFilms() {
        return new ArrayList<>(films.values());
    }
}
package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.director.DirectorDaoStorage;
import ru.yandex.practicum.filmorate.storage.film.FilmDaoStorage;
import ru.yandex.practicum.filmorate.storage.genre.GenreDaoStorage;
import ru.yandex.practicum.filmorate.storage.like.LikeDaoStorage;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DirectorService {

    private final FilmDaoStorage filmDaoStorage;

    private final DirectorDaoStorage directorDaoStorage;

    public List<Film> getDirectorsFilm(Integer directorId, String sortBy) {
        getDirectorById(directorId);
        if(sortBy.equals("year")) {
            return filmDaoStorage.getDirectorsFilmSortByYear(directorId);
        }
        if(sortBy.equals("likes")) {
            return filmDaoStorage.getDirectorsFilmSortByLikesCount(directorId);
        }
        throw new EntityNotFoundException("Некорректные входные данные");
    }

    public List<Director> getDirectors(){
        return directorDaoStorage.allDirectors();
    }

    public Director getDirectorById(Integer id) {
        Director director = directorDaoStorage.findDirectorById(id);
        if(director == null) {
            throw new EntityNotFoundException("Некорректный id");
        }
        return director;
    }

    public Director createDirector(Director director) {
        return directorDaoStorage.createDirector(director);
    }

    public Director updateDirector(Director director) {
        return directorDaoStorage.updateDirector(director);
    }

    public void deleteDirector(Integer id) {
        directorDaoStorage.deleteDirector(id);
    }
}

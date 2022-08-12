package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

@Component
public class Validator {

    public void userValidator(User user) throws ValidationException {
        if(user.getEmail() == null || user.getEmail().isBlank() || !user.getEmail().contains("@")) {
            throw new ValidationException("Введен некорректный email.");
        }
        if(user.getBirthday().isAfter(LocalDate.now())){
            throw new ValidationException("Введена некорректная дата рождения.");
        }
        if(user.getLogin().contains(" ")){
            throw new ValidationException("Логин не может содержать пробелы.");
        }
        if (user.getName().isBlank()||user.getName() == null){
            user.setName(user.getLogin());
        }
    }

    public void filmValidator(Film film) throws ValidationException {
        if(film.getName().isBlank() || film.getName() == null){
            throw new ValidationException("Введено некорректное название фильма. Название фильма не может быть пустым.");
        }
        if(film.getDescription().length() > 200){
            throw new ValidationException("Введено некорректное описание фильма. " +
                    "Описание должно содержать неболее 200 символов.");
        }
        if(film.getDuration() <= 0){
            throw new ValidationException("Введена некорректная продолжительность. " +
                    "Продолжительность должна быть положительной.");
        }
        if(film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28)))
            throw new ValidationException("Введена некорректная дата выхода фильма. " +
                    "Невозможно добавить фильм с датой выхода ранешь, чем 28.12.1985 г.");

        if(film.getMpa() == null){
            throw new ValidationException("Введен некорректный возрастной рейтинг. Добавьте возрастной рейтинг.");
        }
    }
}
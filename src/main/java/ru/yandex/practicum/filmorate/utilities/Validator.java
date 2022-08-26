package ru.yandex.practicum.filmorate.utilities;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

@Component
@Slf4j
public class Validator {

    public static void userValidator(User user) throws ValidationException {
        if (user.getEmail() == null || user.getEmail().isBlank() || !user.getEmail().contains("@")) {
            throw new ValidationException("Введен некорректный email.");
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidationException("Введена некорректная дата рождения.");
        }
        if (user.getLogin().contains(" ")) {
            throw new ValidationException("Логин не может содержать пробелы.");
        }
        if (user.getName().isBlank() || user.getName() == null) {
            user.setName(user.getLogin());
        }
    }

    public static void filmValidator(Film film) throws ValidationException {
        if (film.getName().isBlank() || film.getName() == null) {
            throw new ValidationException("Введено некорректное название фильма. Название фильма не может быть пустым.");
        }
        if (film.getDescription().length() > 200) {
            throw new ValidationException("Введено некорректное описание фильма. " +
                    "Описание должно содержать неболее 200 символов.");
        }
        if (film.getDuration() <= 0) {
            throw new ValidationException("Введена некорректная продолжительность. " +
                    "Продолжительность должна быть положительной.");
        }
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28)))
            throw new ValidationException("Введена некорректная дата выхода фильма. " +
                    "Невозможно добавить фильм с датой выхода ранешь, чем 28.12.1985 г.");

        if (film.getMpa() == null) {
            throw new ValidationException("Введен некорректный возрастной рейтинг. Добавьте возрастной рейтинг.");
        }
    }

    public static void directorValidator(Director director) throws ValidationException {
        if (director.getName().isBlank() || director.getName() == null) {
            throw new ValidationException("Введено некорректное название режиссера." +
                    " Имя режиссера не может быть пустым.");
        }
    }

    public static void validateReview(Review review) {
        boolean isValidate = false;
        String message = "";

        if (review.getContent() == null || review.getContent().isBlank()) {
            message = "Содержание отзыва не указано.";
        } else if (review.getIsPositive() == null) {
            message = "Статус отзыва (позитивный / негативный) не указан.";
        } else if (review.getUserId() == null) {
            message = "Не указан пользователь.";
        } else if (review.getFilmId() == null) {
            message = "Не указан фильм.";
        } else {
            isValidate = true;
        }

        if (!isValidate) {
            log.debug(message);
            throw new ValidationException(message);
        }
    }
}
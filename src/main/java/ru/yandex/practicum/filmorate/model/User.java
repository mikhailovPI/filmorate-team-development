package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.NonNull;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;

@Data
@Pattern(regexp = "[a-zA-Z0-9@_-]+", message = "Email может состоять из букв и цифр")
public class User {
    private long id;
    private String email;
    private String login;
    private String name;
    private LocalDate birthday;
}
package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.NonNull;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
public class User {
    private long id;

    @NotNull(message = "Необходимо указать email.")
    @NotBlank(message = "Email не может быть пустым.")
    @Email(message = "Email должен быть корректным адресом электронной почты.")
    private String email;

    @NotNull(message = "Необходимо указать Login.")
    @NotBlank(message = "Login не может быть пустым.")
    @Pattern(regexp = "^\\S*$")
    @Pattern(regexp = "[a-zA-Z\\d]+", message = "Логин должен состоять из букв и цифр.")
    private String login;

    private String name;

    @NotNull(message = "Необходимо указать дату рождения.")
    @PastOrPresent(message = "Дата рождения не должна быть больше текущей.")
    private LocalDate birthday;
}
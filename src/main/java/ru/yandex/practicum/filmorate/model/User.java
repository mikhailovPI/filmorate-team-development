package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
public class User {
    private long id;

    @NotNull(message = "Необходимо указать email.")
    @NotBlank(message = "Необходимо указать email.")
    @Length(min = 1)
    @Email(message = "Email должен быть корректным адресом электронной почты.")
    private String email;

    @NotNull(message = "Необходимо указать логин.")
    @NotBlank(message = "Необходимо указать логин.")
    @Pattern(regexp = "^\\S*$")
    @Pattern(regexp = "[a-zA-Z\\d]+", message = "Логин может состоять из букв и цифр.")
    @Length(min = 1)
    private String login;

    private String name;

    @NotNull(message = "Необходимо указать дату рождения.")
    @Past(message = "Дата рождения не должна быть больше текущей.")
    private LocalDate birthday;
}
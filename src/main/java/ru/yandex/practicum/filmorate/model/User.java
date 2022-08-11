package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Builder;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
@Builder
public class User {

    private Long id;

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

    private Set<Long> friends = new HashSet<>();
}
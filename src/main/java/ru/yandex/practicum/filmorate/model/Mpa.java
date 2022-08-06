package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@EqualsAndHashCode(of = "mpaId")
public class Mpa {

    private final Integer mpaId;

    @NotBlank(message = "Возрастное ограничение не может быть пустым.")
    private final String mpaName;
}

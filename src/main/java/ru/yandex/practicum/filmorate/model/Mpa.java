package ru.yandex.practicum.filmorate.model;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Data
@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Mpa {

    private Integer id;

    @NotBlank(message = "Возрастное ограничение не может быть пустым.")
    private String name;
}

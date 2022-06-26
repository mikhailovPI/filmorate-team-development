package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
public class Film {
    private long id;

    @NotNull(message = "Необходимо указать название фильма.")
    @NotBlank(message = "Необходимо указать название фильма.")
    @Length(min = 1)
    private String name;

    @NotNull(message = "Необходимо указать описание фильма.")
    @NotBlank(message = "Необходимо указать описание фильма.")
    @Length(min = 1, max = 200)
    private String description;

    @NotNull(message = "Необходимо указать дату релиза фильма.")
    private LocalDate releaseDate;

    @Min(1)
    private long duration;
}
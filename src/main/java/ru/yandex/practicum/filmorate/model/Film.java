package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@EqualsAndHashCode(of = "filmId")
public class Film {

    private Long filmId;

    @NotNull(message = "Необходимо указать название фильма.")
    @NotBlank(message = "Название фильма не может быть пустым.")
    private String filmName;

    @NotNull(message = "Необходимо указать описание фильма.")
    @NotBlank(message = "Описание фильма не может быть пустым.")
    @Length(max = 200)
    private String description;

    @NotNull(message = "Необходимо указать дату релиза фильма.")
    private LocalDate releaseDate;

    @Min(1)
    private Long duration;

    private final Set<Long> like = new HashSet<>();

    @NotNull(message = "Необходимо указать жанр фильма.")
    private List<Genre> genreFilm;


    @NotNull(message = "Необходимо указать возрастное ограничение фильма.")
    private Mpa mpa;



}
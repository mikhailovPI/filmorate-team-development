package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Builder;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
@Builder
public class Film {

    private Set<Long> like = new HashSet<>();

    private Set<Genre> genres = new HashSet<>();

    private Long id;

    @NotNull(message = "Необходимо указать название фильма.")
    @NotBlank(message = "Название фильма не может быть пустым.")
    private String name;

    @NotNull(message = "Необходимо указать описание фильма.")
    @NotBlank(message = "Описание фильма не может быть пустым.")
    @Length(max = 200)
    private String description;


    @NotNull(message = "Необходимо указать дату релиза фильма.")
    private LocalDate releaseDate;

    @Min(1)
    private Integer duration;

    //@NotNull(message = "Необходимо указать возрастное ограничение фильма.")
    private Mpa mpa;






/*    public Film(Long id, String name, LocalDate releaseDate, String description, Long duration, Mpa mpa) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.mpa = mpa;
    }*/

/*    public Film(Long id, String name, String description, LocalDate releaseDate, Long duration, Set<Genre> genres) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.genres = genres;
    }*/
}
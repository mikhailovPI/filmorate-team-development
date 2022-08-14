package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.EqualsAndHashCode;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class Film {

    private Set<Long> likes = new HashSet<>();

    private Set<Genre> genres;

    private Long id;

    private String name;

    private String description;

    private LocalDate releaseDate;

    private Integer duration;

    private Mpa mpa;

//    public void addLike(Long userId) {
//        likes.add(userId);
//    }

    public Long getLikeCount() {
        return (long) likes.size();
    }
}
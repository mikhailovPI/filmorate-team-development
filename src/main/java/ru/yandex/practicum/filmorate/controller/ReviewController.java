package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.service.ReviewService;

import javax.validation.Valid;
import java.util.Collection;

@Slf4j
@RestController
@RequestMapping("/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    @Autowired
    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping()
    public Review addReview(@Valid @RequestBody Review review) {
        return reviewService.addReview(review);
    }

    @PutMapping()
    public Review updateReview(@Valid @RequestBody Review review) {
        return reviewService.updateReview(review);
    }

    @DeleteMapping(value = "/{id}")
    public void deleteReview (@PathVariable Long id) {
        reviewService.deleteReview(id);
    }

    @GetMapping(value = "/{id}")
    public Review getReviewById (@PathVariable Long id) {
        return reviewService.getReviewById(id);
    }

    @GetMapping()
    public Collection<Review> getReviewsOfFilm (
            @RequestParam(value = "filmId", defaultValue = "0", required = false) Long filmId,
            @RequestParam (value = "count", defaultValue = "10", required = false) Integer count) {
        if (filmId<0 || count <1) {
            throw new IllegalArgumentException("Некорректный аргумент");
        }
        return reviewService.getReviewsOfFilm(filmId, count);
    }

    @PutMapping(value = "/{id}/like/{userId}")
    public void likeReview(@PathVariable Long id, @PathVariable Long userId) {
        reviewService.likeReview(id, userId);
    }

    @PutMapping(value = "/{id}/dislike/{userId}")
    public void dislikeReview(@PathVariable Long id, @PathVariable Long userId) {
        reviewService.dislikeReview(id, userId);
    }

    @DeleteMapping(value = "/{id}/like/{userId}")
    public void deleteLike(@PathVariable Long id, @PathVariable Long userId) {
        reviewService.deleteLike(id, userId);
    }

    @DeleteMapping(value = "/{id}/dislike/{userId}")
    public void deleteDislike(@PathVariable Long id, @PathVariable Long userId) {
        reviewService.deleteDislike(id, userId);
    }
}

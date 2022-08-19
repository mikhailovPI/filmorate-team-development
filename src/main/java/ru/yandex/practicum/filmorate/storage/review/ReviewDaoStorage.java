package ru.yandex.practicum.filmorate.storage.review;

import ru.yandex.practicum.filmorate.model.Review;

import java.util.Collection;

public interface ReviewDaoStorage {
    Review addReview(Review review);

    Review updateReview(Review review);

    void deleteReview(long reviewId);

    Review getReviewById(long reviewId);

    Collection<Review> getReviewsOfFilm(long filmId, int limit);

    void likeReview(long reviewId, long userId);

    void dislikeReview(long reviewId, long userId);

    void deleteLike(long reviewId, long userId);

    void deleteDislike(long reviewId, long userId);
}

package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.storage.review.ReviewDaoStorage;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewDaoStorage reviewDaoStorage;

    public Review addReview(Review review) {
        return reviewDaoStorage.addReview(review);
    }

    public Review updateReview(Review review) {
        return reviewDaoStorage.updateReview(review);
    }

    public void deleteReview(long reviewId) {
        reviewDaoStorage.deleteReview(reviewId);
    }

    public Review getReviewById(long reviewId) {
        return reviewDaoStorage.getReviewById(reviewId);
    }

    public Collection<Review> getReviewsOfFilm(long filmId, int limit) {
        return reviewDaoStorage.getReviewsOfFilm(filmId, limit);
    }

    public void likeReview(long reviewId, long userId) {
        reviewDaoStorage.likeReview(reviewId, userId);
    }

    public void dislikeReview(long reviewId, long userId) {
        reviewDaoStorage.dislikeReview(reviewId, userId);
    }

    public void deleteLike(long reviewId, long userId) {
        reviewDaoStorage.deleteLike(reviewId, userId);
    }

    public void deleteDislike(long reviewId, long userId) {
        reviewDaoStorage.deleteDislike(reviewId, userId);
    }
}

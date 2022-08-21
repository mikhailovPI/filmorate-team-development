package ru.yandex.practicum.filmorate.storage.review;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.model.enums.EventType;
import ru.yandex.practicum.filmorate.model.enums.OperationType;
import ru.yandex.practicum.filmorate.storage.Validator;
import ru.yandex.practicum.filmorate.storage.feed.FeedDaoStorage;
import ru.yandex.practicum.filmorate.utilities.Checker;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

@Repository
@Slf4j
public class ReviewDbStorage implements ReviewDaoStorage {

    private final JdbcTemplate jdbcTemplate;
    private final Validator validator;

    private final FeedDaoStorage feedDaoStorage;

    public ReviewDbStorage(JdbcTemplate jdbcTemplate, Validator validator, FeedDaoStorage feedDaoStorage) {
        this.jdbcTemplate = jdbcTemplate;
        this.validator = validator;
        this.feedDaoStorage = feedDaoStorage;
    }

    @Override
    public Review addReview(Review review) {
        validator.validateReview(review);
        review.setUseful(0);

        Checker.checkUserExists(review.getUserId(), jdbcTemplate);
        Checker.checkFilmExists(review.getFilmId(), jdbcTemplate);

        final var sqlQuery = "INSERT INTO REVIEW (REVIEW_CONTENT, " +
                "REVIEW_IS_POSITIVE, " +
                "USER_ID, " +
                "FILM_ID, REVIEW_USEFUL) VALUES (?, ?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"REVIEW_ID"});
            stmt.setString(1, review.getContent());
            stmt.setBoolean(2, review.getIsPositive());
            stmt.setLong(3, review.getUserId());
            stmt.setLong(4, review.getFilmId());
            stmt.setInt(5, review.getUseful());
            return stmt;
        }, keyHolder);
        review.setReviewId(keyHolder.getKey().longValue());
        feedDaoStorage.addFeed(review.getUserId(), review.getReviewId(), EventType.REVIEW, OperationType.ADD);

        return review;
    }

    @Override
    public Review updateReview(Review review) {
        validator.validateReview(review);
        checkReviewExists(review.getReviewId());

        Checker.checkUserExists(review.getUserId(), jdbcTemplate);
        Checker.checkFilmExists(review.getFilmId(), jdbcTemplate);

        final var sqlQuery = "UPDATE REVIEW " +
                "SET REVIEW_CONTENT = ?, " +
                "REVIEW_IS_POSITIVE = ? WHERE REVIEW_ID = ?";

        jdbcTemplate.update(sqlQuery,
                review.getContent(),
                review.getIsPositive(),
                review.getReviewId());

        var reviewUpdated = getReviewById(review.getReviewId());

        feedDaoStorage.addFeed(reviewUpdated.getUserId(),
                reviewUpdated.getReviewId(),
                EventType.REVIEW,
                OperationType.UPDATE);

        return reviewUpdated;
    }

    @Override
    public void deleteReview(long reviewId) {
        checkReviewExists(reviewId);
        var review = getReviewById(reviewId);

        final var sqlQuery = "DELETE FROM REVIEW WHERE REVIEW_ID =?";

        feedDaoStorage.addFeed(review.getUserId(), review.getReviewId(), EventType.REVIEW, OperationType.REMOVE);
        jdbcTemplate.update(sqlQuery, reviewId);
    }

    @Override
    public Review getReviewById(long reviewId) {
        checkReviewExists(reviewId);
        final var sqlQuery = "SELECT * FROM REVIEW WHERE REVIEW_ID = ?";
        return jdbcTemplate.queryForObject(sqlQuery, (rs, rowNum) -> makeReview(rs), reviewId);
    }

    @Override
    public Collection<Review> getReviewsOfFilm(long filmId, int count) {
        if (filmId != 0) {
            Checker.checkFilmExists(filmId, jdbcTemplate);
            final var sqlQuery = "SELECT * FROM REVIEW WHERE FILM_ID = ? ORDER BY REVIEW_USEFUL DESC LIMIT ?";
            return jdbcTemplate.query(sqlQuery, (rs, rowNum) -> makeReview(rs), filmId, count);
        } else {
            final var sqlQuery = "SELECT * FROM REVIEW ORDER BY REVIEW_USEFUL DESC LIMIT ?";
            return jdbcTemplate.query(sqlQuery, (rs, rowNum) -> makeReview(rs), count);
        }
    }

    @Override
    public void likeReview(long reviewId, long userId) {
        var estimate = getEstimate(reviewId, userId);

        if (estimate != null) {
            var message = String.format("Повторная оценка не возможна без удаления текущей (%s).", estimate);
            throw new RuntimeException(message);
        }

        final var sqlQuery = "INSERT INTO REVIEW_LIKES (REVIEW_ID, USER_ID, IS_LIKE) VALUES (?, ?, ?)";
        jdbcTemplate.update(sqlQuery, reviewId, userId, true);

        final var sqlQueryForUsefulUpd = "UPDATE REVIEW SET REVIEW_USEFUL = REVIEW_USEFUL + 1 WHERE REVIEW_ID = ?";
        jdbcTemplate.update(sqlQueryForUsefulUpd, reviewId);
    }

    @Override
    public void dislikeReview(long reviewId, long userId) {
        var estimate = getEstimate(reviewId, userId);

        if (estimate != null) {
            var message = String.format("Повторная оценка не возможна без удаления текущей (%s).", estimate);
            throw new RuntimeException(message);
        }

        final var sqlQuery = "INSERT INTO REVIEW_LIKES (REVIEW_ID, USER_ID, IS_LIKE) VALUES (?, ?, ?)";
        jdbcTemplate.update(sqlQuery, reviewId, userId, false);

        final var sqlQueryForUsefulUpd = "UPDATE REVIEW SET REVIEW_USEFUL = REVIEW_USEFUL - 1 WHERE REVIEW_ID = ?";
        jdbcTemplate.update(sqlQueryForUsefulUpd, reviewId);
    }

    @Override
    public void deleteLike(long reviewId, long userId) {
        var estimate = getEstimate(reviewId, userId);

        if (estimate == null) {
            var message = "Не возможно удаление не существующей оценки.";
            throw new RuntimeException(message);
        }

        if (!estimate) {
            var message = String.format("Пользователь %s оценил отзыв %s как %s. " +
                            "Данная операция не позволяет удалить отрицательный отзыв.",
                    userId,
                    reviewId,
                    false);
            throw new RuntimeException(message);
        }

        final var sqlQuery = "DELETE FROM REVIEW_LIKES WHERE REVIEW_ID=? AND USER_ID = ?";
        jdbcTemplate.update(sqlQuery, reviewId, userId);

        final var sqlQueryForUsefulUpd = "UPDATE REVIEW SET REVIEW_USEFUL = REVIEW_USEFUL -1 WHERE REVIEW_ID = ?";
        jdbcTemplate.update(sqlQueryForUsefulUpd, reviewId);
    }

    @Override
    public void deleteDislike(long reviewId, long userId) {
        var estimate = getEstimate(reviewId, userId);

        if (estimate == null) {
            var message = "Не возможно удаление не существующей оценки.";
            throw new RuntimeException(message);
        }

        if (estimate) {
            throw new RuntimeException(String.format("Пользователь %s оценил отзыв %s как %s. " +
                            "Данная операция не позволяет удалить положительный отзыв.",
                    userId,
                    reviewId,
                    true));
        }

        final var sqlQuery = "DELETE FROM REVIEW_LIKES WHERE REVIEW_ID=? AND USER_ID = ?";
        jdbcTemplate.update(sqlQuery, reviewId, userId);

        final var sqlQueryForUsefulUpd = "UPDATE REVIEW SET REVIEW_USEFUL = REVIEW_USEFUL + 1 WHERE REVIEW_ID = ?";
        jdbcTemplate.update(sqlQueryForUsefulUpd, reviewId);
    }

    private Review makeReview(ResultSet rs) throws SQLException {
        Review review = new Review();
        review.setReviewId(rs.getLong("REVIEW_ID"));
        review.setContent(rs.getString("REVIEW_CONTENT"));
        review.setIsPositive(rs.getBoolean("REVIEW_IS_POSITIVE"));
        review.setUserId(rs.getLong("USER_ID"));
        review.setFilmId(rs.getLong("FILM_ID"));
        review.setUseful(rs.getInt("REVIEW_USEFUL"));
        return review;
    }

    private void checkReviewExists(Long reviewID) {
        String sqlQuery = "SELECT * FROM REVIEW WHERE REVIEW_ID =?";
        if (!jdbcTemplate.queryForRowSet(sqlQuery, reviewID).next()) {
            log.debug("Отзыв с id: {} не найден.", reviewID);
            throw new EntityNotFoundException((String.format("Отзыв с id: %s не найден.", reviewID)));
        }
    }

    private Boolean getEstimate(long reviewId, long userId) {
        checkReviewExists(reviewId);
        Checker.checkUserExists(userId, jdbcTemplate);

        final var sqlQuery = "SELECT IS_LIKE FROM REVIEW_LIKES WHERE REVIEW_ID=? AND USER_ID=?";

        var estimatesList = jdbcTemplate.query(sqlQuery, (rs, rowNum) -> rs.getBoolean("IS_LIKE"),
                reviewId, userId);
        if (estimatesList.size() != 0) {
            var estimate = estimatesList.get(0);
            var message = String.format("Пользователь %s оценил отзыв %s как %s.", userId, reviewId, estimate);

            log.debug(message);
            return estimate;
        }
        var message = String.format("Пользователь %s не оценивал отзыв %s.", userId, reviewId);

        log.debug(message);
        return null;
    }
}

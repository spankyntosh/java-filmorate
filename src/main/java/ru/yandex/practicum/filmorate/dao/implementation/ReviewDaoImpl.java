package ru.yandex.practicum.filmorate.dao.implementation;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.constants.ReviewConstants;
import ru.yandex.practicum.filmorate.dao.ReviewDAO;
import ru.yandex.practicum.filmorate.dao.mappers.ReviewRowMapper;
import ru.yandex.practicum.filmorate.exceptions.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.Review;

import java.util.List;
import java.util.Map;

@Repository
@Primary
@RequiredArgsConstructor
public class ReviewDaoImpl implements ReviewDAO<Review> {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Review> getAll() {
        return jdbcTemplate.query(
                ReviewConstants.GET_REVIEWS,
                new ReviewRowMapper()
        );
    }

    @Override
    public List<Review> getByParams(Map<String, Integer> params) {
        StringBuilder queryString = new StringBuilder(ReviewConstants.GET_REVIEWS_BY_FILM_ID);
        int limit = 10;
        if (!params.isEmpty()) {
            if (params.containsKey("filmId")) {
                queryString.append(" WHERE ")
                        .append("film_id = ")
                        .append(params.get("filmId"));
            }

            queryString.append(" ORDER BY useful DESC");

            if (params.containsKey("count")) {
                limit = Integer.parseInt(String.valueOf(params.get("count")));
            }
        } else {
            queryString.append(" ORDER BY useful DESC");
        }

        queryString.append(" LIMIT ").append(limit);

        return jdbcTemplate.query(
                queryString.toString(),
                new ReviewRowMapper()
        );
    }

    @Override
    public Review create(Review review) {
        if (!checkFilmExists(review.getFilmId())) {
            throw new EntityNotFoundException("Фильм с идентификатором " + review.getFilmId() + " отсутствует");
        }
        if (!checkUserExists(review.getUserId())) {
            throw new EntityNotFoundException("Пользователь с идентификатором " + review.getUserId() + " отсутствует");
        }
        createOrUpdate(
                ReviewConstants.CREATE_REVIEW,
                new Object[]{
                        review.getUserId(),
                        review.getFilmId(),
                        review.getContent(),
                        review.getIsPositive(),
                        0
                }
        );

        return jdbcTemplate.query(
                        ReviewConstants.GET_LAST_REVIEW,
                        new ReviewRowMapper()
                )
                .stream()
                .findAny()
                .orElseThrow(() -> new EntityNotFoundException("Ничего не найдено!"));
    }

    @Override
    public Review update(Review review) {
        if (!checkFilmExists(review.getFilmId())) {
            throw new EntityNotFoundException("Фильм с идентификатором " + review.getFilmId() + " отсутствует");
        }
        if (!checkUserExists(review.getUserId())) {
            throw new EntityNotFoundException("Пользователь с идентификатором " + review.getUserId() + " отсутствует");
        }
        createOrUpdate(
                ReviewConstants.UPDATE_REVIEW,
                new Object[]{
                        review.getContent(),
                        review.getIsPositive(),
                        0,
                        review.getReviewId()
                }
        );

        return jdbcTemplate.query(
                        ReviewConstants.GET_LAST_REVIEW_AFTER_UPDATE,
                        new ReviewRowMapper(),
                        review.getReviewId()
                )
                .stream()
                .findAny()
                .orElseThrow(() -> new EntityNotFoundException("Ничего не найдено!"));
    }

    @Override
    public Review getById(Long reviewId) {
        return jdbcTemplate.query(
                        ReviewConstants.GET_REVIEWS_BY_ID,
                        new ReviewRowMapper(),
                        reviewId
                )
                .stream()
                .findAny()
                .orElseThrow(() -> new EntityNotFoundException("Отзыв не найден"));
    }

    @Override
    public void addLikeToReview(Integer reviewId, Integer userId) {
        if (!checkUserExists(userId)) {
            throw new EntityNotFoundException("Пользователь с идентификатором " + userId + " отсутствует");
        }
        if (!checkReviewExists(reviewId)) {
            throw new EntityNotFoundException("Отзыв с идентификатором " + reviewId + " отсутствует");
        }

        jdbcTemplate.update(
                ReviewConstants.INSERT_LIKE_TO_REVIEW,
                reviewId,
                userId
        );

        jdbcTemplate.update(
                ReviewConstants.UPDATE_REVIEW_AFTER_ADD_LIKE,
                reviewId
        );

        jdbcTemplate.update(
                ReviewConstants.DELETE_DISLIKE_FROM_REVIEW,
                reviewId,
                userId
        );
    }

    @Override
    public void addDisLikeToReview(Integer reviewId, Integer userId) {
        if (!checkUserExists(userId)) {
            throw new EntityNotFoundException("Пользователь с идентификатором " + userId + " отсутствует");
        }
        if (!checkReviewExists(reviewId)) {
            throw new EntityNotFoundException("Отзыв с идентификатором " + reviewId + " отсутствует");
        }

        jdbcTemplate.update(
                ReviewConstants.INSERT_DISLIKE_TO_REVIEW,
                reviewId,
                userId
        );

        jdbcTemplate.update(
                ReviewConstants.UPDATE_REVIEW_AFTER_ADD_DISLIKE,
                reviewId
        );

        jdbcTemplate.update(
                ReviewConstants.DELETE_LIKE_FROM_REVIEW,
                reviewId,
                userId
        );
    }

    @Override
    public void deleteLikeToReview(Integer reviewId, Integer userId) {
        if (!checkUserExists(userId)) {
            throw new EntityNotFoundException("Пользователь с идентификатором " + userId + " отсутствует");
        }
        if (!checkReviewExists(reviewId)) {
            throw new EntityNotFoundException("Отзыв с идентификатором " + reviewId + " отсутствует");
        }

        jdbcTemplate.update(
                ReviewConstants.DELETE_LIKE_FROM_REVIEW,
                reviewId,
                userId
        );

        jdbcTemplate.update(
                ReviewConstants.UPDATE_REVIEW_AFTER_DELETE_LIKE,
                reviewId
        );
    }

    @Override
    public void deleteDisLikeToReview(Integer reviewId, Integer userId) {
        if (!checkUserExists(userId)) {
            throw new EntityNotFoundException("Пользователь с идентификатором " + userId + " отсутствует");
        }
        if (!checkReviewExists(reviewId)) {
            throw new EntityNotFoundException("Отзыв с идентификатором " + reviewId + " отсутствует");
        }

        jdbcTemplate.update(
                ReviewConstants.DELETE_DISLIKE_FROM_REVIEW,
                reviewId,
                userId
        );

        jdbcTemplate.update(
                ReviewConstants.UPDATE_REVIEW_AFTER_DELETE_DISLIKE,
                reviewId
        );
    }

    @Override
    public void delete(Long reviewId) {
        if (!checkReviewExists(Math.toIntExact(reviewId))) {
            throw new EntityNotFoundException("Отзыв с идентификатором " + reviewId + " отсутствует");
        }
        jdbcTemplate.update(
                "DELETE FROM reviews WHERE review_id = ?",
                reviewId
        );
    }

    private Boolean checkReviewExists(Integer reviewId) {
        Integer reviewCount = jdbcTemplate.queryForObject(
                ReviewConstants.GET_REVIEW_COUNT_ID,
                Integer.class,
                reviewId
        );
        return reviewCount != null && reviewCount > 0;
    }

    private Boolean checkFilmExists(Integer filmId) {
        Integer filmsCount = jdbcTemplate.queryForObject(
                ReviewConstants.GET_FILM_BY_ID,
                Integer.class,
                filmId
        );

        return filmsCount != null && filmsCount > 0;
    }

    private Boolean checkUserExists(Integer userId) {
        Integer usersCount = jdbcTemplate.queryForObject(
                ReviewConstants.GET_USER_BY_ID,
                Integer.class,
                userId
        );

        return usersCount != null && usersCount > 0;
    }

    private void createOrUpdate(String query, Object[] obj) {
        jdbcTemplate.update(
                query,
                obj

        );
    }
}
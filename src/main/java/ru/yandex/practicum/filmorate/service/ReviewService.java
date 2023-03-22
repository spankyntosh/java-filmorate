package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.EventDAO;
import ru.yandex.practicum.filmorate.dao.ReviewDAO;
import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.model.EventType;
import ru.yandex.practicum.filmorate.model.Operation;
import ru.yandex.practicum.filmorate.model.Review;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewDAO<Review> reviewStorage;
    private final EventDAO eventDAO;

    public List<Review> getAll() {
        return reviewStorage.getAll();
    }

    public List<Review> findReviewsByParams(Map<String, Integer> params) {
        return reviewStorage.getByParams(params);
    }

    public Review create(Review review) {
        Review newReview = reviewStorage.create(review);
        Event event = new Event()
                .toBuilder()
                .userId(newReview.getUserId())
                .entityId(newReview.getReviewId().intValue())
                .timestamp(System.currentTimeMillis())
                .eventType(EventType.REVIEW)
                .operation(Operation.ADD)
                .build();
        eventDAO.addEvent(event);
        return newReview;

    }

    public Review update(Review review) {
        Review upReview = reviewStorage.update(review);
        Event event = new Event()
                .toBuilder()
                .userId(upReview.getUserId())
                .entityId(upReview.getReviewId().intValue())
                .timestamp(System.currentTimeMillis())
                .eventType(EventType.REVIEW)
                .operation(Operation.UPDATE)
                .build();
        eventDAO.addEvent(event);
        return reviewStorage.update(review);
    }

    public Review getByReviewId(Long reviewId) {
        return reviewStorage.getById(reviewId);
    }

    public void addLikeToReview(Integer reviewId, Integer userId) {
        reviewStorage.addLikeToReview(reviewId, userId);
    }

    public void addDisLikeToReview(Integer reviewId, Integer userId) {
        reviewStorage.addDisLikeToReview(reviewId, userId);
    }

    public void deleteLikeToReview(Integer reviewId, Integer userId) {
        reviewStorage.deleteLikeToReview(reviewId, userId);
    }

    public void deleteDisLikeToReview(Integer reviewId, Integer userId) {
        reviewStorage.deleteDisLikeToReview(reviewId, userId);
    }

    public void delete(Long reviewId) {
        Review review = getByReviewId(reviewId);
        Event event = new Event()
                .toBuilder()
                .userId(review.getUserId())
                .entityId(review.getReviewId().intValue())
                .timestamp(System.currentTimeMillis())
                .eventType(EventType.REVIEW)
                .operation(Operation.REMOVE)
                .build();
        eventDAO.addEvent(event);
        reviewStorage.delete(reviewId);
    }
}
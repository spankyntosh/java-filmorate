package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.Review;
import java.util.List;
import java.util.Map;

public interface ReviewDAO<T extends Review> {
    List<T> getAll();

    List<T> getByParams(Map<String, Integer> params);

    T create(Review review);

    T update(Review review);

    T getById(Long reviewId);

    void addLikeToReview(Integer reviewId, Integer userId);

    void addDisLikeToReview(Integer reviewId, Integer userId);

    void deleteLikeToReview(Integer reviewId, Integer userId);

    void deleteDisLikeToReview(Integer reviewId, Integer userId);

    void delete(Long reviewId);
}
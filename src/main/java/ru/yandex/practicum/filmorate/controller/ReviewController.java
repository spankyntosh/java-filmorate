package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.service.ReviewService;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/reviews")
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;

    @GetMapping
    public List<Review> getReviewsByParams(@RequestParam(required = false) Map<String, Integer> params) {
        log.info("Endpoint request received: 'GET .reviews with params: {}'", params.toString());
        return reviewService.findReviewsByParams(params);
    }

    @PostMapping
    public Review create(@Valid @RequestBody Review review) {
        log.info("Endpoint request received: 'POST reviews'");
        return reviewService.create(review);
    }

    @GetMapping("/{reviewId}")
    public Review getReviewById(@PathVariable Long reviewId) {
        log.info("Endpoint request received: 'GET reviews/{}'", reviewId);
        return reviewService.getByReviewId(reviewId);
    }

    @PutMapping
    public Review update(@Valid @RequestBody Review review) {
        log.info("Endpoint request received: 'PUT reviews/{}'", review.toString());
        return reviewService.update(review);
    }

    @PutMapping("/{reviewId}/like/{userId}")
    public void addLikeToReview(@PathVariable Integer reviewId, @PathVariable Integer userId) {
        log.info("Endpoint request received: 'PUT reviews/{}/like/{}'", reviewId, userId);
        reviewService.addLikeToReview(reviewId, userId);
    }

    @PutMapping("/{reviewId}/dislike/{userId}")
    public void addDisLikeToReview(@PathVariable Integer reviewId, @PathVariable Integer userId) {
        log.info("Endpoint request received: 'PUT reviews/{}/dislike/{}'", reviewId, userId);
        reviewService.addDisLikeToReview(reviewId, userId);
    }

    @DeleteMapping("/{reviewId}/like/{userId}")
    public void deleteLikeToReview(@PathVariable Integer reviewId, @PathVariable Integer userId) {
        log.info("Endpoint request received: 'DELETE reviews/{}/like/{}'", reviewId, userId);
        reviewService.deleteLikeToReview(reviewId, userId);
    }

    @DeleteMapping("/{reviewId}/dislike/{userId}")
    public void deleteDisLikeToReview(@PathVariable Integer reviewId, @PathVariable Integer userId) {
        log.info("Endpoint request received: 'DELETE reviews/{}/dislike/{}'", reviewId, userId);
        reviewService.deleteDisLikeToReview(reviewId, userId);
    }

    @DeleteMapping("/{reviewId}")
    public void removeReview(@PathVariable Long reviewId) {
        log.info("Endpoint request received: 'DELETE reviews/{}'", reviewId);
        reviewService.delete(reviewId);
    }
}
package ru.yandex.practicum.filmrate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmrate.model.Review;
import ru.yandex.practicum.filmrate.service.ReviewService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/reviews")
@Slf4j
public class ReviewController {
	private final ReviewService reviewService;

	@Autowired
	public ReviewController(ReviewService reviewService) {
		this.reviewService = reviewService;
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Review addReview(@Valid @RequestBody Review review) {
		Review addedReview = reviewService.addReview(review);
		log.info("Отзыв с id={} был добавлен.", addedReview.getReviewId());
		return addedReview;
	}

	@PutMapping
	@ResponseStatus(HttpStatus.OK)
	public Review updateReview(@Valid @RequestBody Review review) {
		Review updatedReview = reviewService.updateReview(review);
		log.info("Отзыв с id={} обновлён.", updatedReview.getReviewId());
		return updatedReview;
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteReview(@PathVariable int id) {
		reviewService.deleteReview(id);
		log.info("Отзыв c id={} был удалён.", id);
	}

	@GetMapping("/{id}")
	@ResponseStatus(HttpStatus.OK)
	public Review getReviewById(@PathVariable int id) {
		Review foundReview = reviewService.getReviewById(id);
		log.info("Отзыв с id={} получен.", foundReview.getReviewId());
		return foundReview;
	}

	@GetMapping()
	@ResponseStatus(HttpStatus.OK)
	public List<Review> getReviewsForFilm(@RequestParam(defaultValue = "0") int filmId,
	                                      @RequestParam(defaultValue = "10") int count) {
		log.info("Получение списка отзывов для фильма с id={} ", filmId);
		return reviewService.getReviewsForFilm(filmId, count);
	}

	@PutMapping("/{id}/like/{userId}")
	@ResponseStatus(HttpStatus.OK)
	public void addLikeReview(@PathVariable int id,
	                          @PathVariable int userId) {
		log.info("Пользователь с id={} добавил лайк отзыву с id={}", id, userId);
		reviewService.addLikeReview(id, userId);
	}

	@PutMapping("/{id}/dislike/{userId}")
	@ResponseStatus(HttpStatus.OK)
	public void addDislikeReview(@PathVariable int id,
	                             @PathVariable int userId) {
		log.info("Пользователь с id={} добавил дизлайк отзыву с id={}", id, userId);
		reviewService.addDislikeReview(id, userId);
	}

	@DeleteMapping("/{id}/like/{userId}")
	@ResponseStatus(HttpStatus.OK)
	public void deleteLikeReview(@PathVariable int id,
	                             @PathVariable int userId) {
		log.info("Пользователь с id={} удалил лайк с отзыва id={}", id, userId);
		reviewService.deleteLikeReview(id, userId);
	}

	@DeleteMapping("/reviews/{id}/dislike/{userId}")
	@ResponseStatus(HttpStatus.OK)
	public void deleteDislikeReview(@PathVariable int id,
	                                @PathVariable int userId) {
		log.info("Пользователь с id={} удалил дизлайк с отзыва id={}", id, userId);
		reviewService.deleteDislikeReview(id, userId);
	}
}
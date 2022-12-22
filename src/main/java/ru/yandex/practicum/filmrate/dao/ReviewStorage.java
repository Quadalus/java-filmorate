package ru.yandex.practicum.filmrate.dao;

import ru.yandex.practicum.filmrate.model.Review;

import java.util.List;
import java.util.Optional;

public interface ReviewStorage {
	Review addReview(Review review);

	Review updateReview(Review review);

	void deleteReview(int reviewId);

	Optional<Review> getReviewById(int reviewId);

	List<Review> getReviewsForFilm(int filmId, int count);
}
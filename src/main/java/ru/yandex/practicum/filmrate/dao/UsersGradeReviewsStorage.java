package ru.yandex.practicum.filmrate.dao;

public interface UsersGradeReviewsStorage {
	void addLikeReview(int reviewId, int userId);

	void addDislikeReview(int reviewId, int userId);

	void deleteLikeReview(int reviewId, int userId);

	void deleteDislikeReview(int reviewId, int userId);
}
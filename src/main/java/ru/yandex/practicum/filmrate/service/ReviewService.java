package ru.yandex.practicum.filmrate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmrate.dao.EventStorage;
import ru.yandex.practicum.filmrate.dao.impl.FilmDbStorage;
import ru.yandex.practicum.filmrate.dao.impl.ReviewDbStorage;
import ru.yandex.practicum.filmrate.dao.impl.UserDbStorage;
import ru.yandex.practicum.filmrate.dao.impl.UsersGradeReviewsDbStorage;
import ru.yandex.practicum.filmrate.exception.NotFoundException;
import ru.yandex.practicum.filmrate.exception.ValidationException;
import ru.yandex.practicum.filmrate.model.Event;
import ru.yandex.practicum.filmrate.model.EventOperation;
import ru.yandex.practicum.filmrate.model.EventType;
import ru.yandex.practicum.filmrate.model.Review;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {
	private final ReviewDbStorage reviewDbStorage;
	private final UsersGradeReviewsDbStorage usersGradeReviewsDbStorage;
	private final UserDbStorage userDbStorage;
	private final FilmDbStorage filmDbStorage;

	private final EventStorage eventStorage;

	public Review addReview(Review review) {
		if ((review.getUserId() != null) && (review.getUserId() > 0) && (userDbStorage.getUserById(review.getUserId()).isPresent())) {
			if ((review.getFilmId() != null) && (review.getFilmId() > 0) && (filmDbStorage.getFilmById(review.getFilmId()).isPresent())) {
				Review createdReview = reviewDbStorage.addReview(review);
				eventStorage.addEvent(Event.builder()
						.eventType(EventType.REVIEW)
						.operation(EventOperation.ADD)
						.userId(review.getUserId())
						.entityId(createdReview.getReviewId())
						.build());
				return createdReview;
			} else if (review.getFilmId() == null) {
				throw new ValidationException("id фильма не передан");
			} else throw new NotFoundException("Передан некорректный id фильма");
		} else if (review.getUserId() == null) {
			throw new ValidationException("id пользователя не передан");
		} else throw new NotFoundException("Передан некорректный id пользователя");
	}

	public Review updateReview(Review review) {
		Review updatedReview = reviewDbStorage.updateReview(review);
		eventStorage.addEvent(Event.builder()
				.eventType(EventType.REVIEW)
				.operation(EventOperation.UPDATE)
				.userId(updatedReview.getUserId())
				.entityId(updatedReview.getReviewId())
				.build());
		return updatedReview;
	}

	public void deleteReview(int reviewId) {
		eventStorage.addEvent(Event.builder()
				.eventType(EventType.REVIEW)
				.operation(EventOperation.REMOVE)
				.userId(getReviewById(reviewId).getUserId())
				.entityId(reviewId)
				.build());
		reviewDbStorage.deleteReview(reviewId);
	}

	public Review getReviewById(int reviewId) {
		return reviewDbStorage.getReviewById(reviewId)
				.orElseThrow(() -> new NotFoundException(String.format("Невозможно получить отзыв с id=%d", reviewId)));
	}

	public List<Review> getReviewsForFilm(int filmId, int count) {
		if (count > 0) {
			return reviewDbStorage.getReviewsForFilm(filmId, count);
		} else throw new ValidationException("Значение параметра count указано некорректно: " + count);
	}

	public void addLikeReview(int reviewId, int userId) {
		usersGradeReviewsDbStorage.addLikeReview(reviewId, userId);
	}

	public void addDislikeReview(int reviewId, int userId) {
		usersGradeReviewsDbStorage.addDislikeReview(reviewId, userId);

	}

	public void deleteLikeReview(int reviewId, int userId) {
		usersGradeReviewsDbStorage.deleteLikeReview(reviewId, userId);
		eventStorage.addEvent(Event.builder()
				.eventType(EventType.LIKE)
				.operation(EventOperation.REMOVE)
				.userId(userId)
				.entityId(reviewId)
				.build());
	}

	public void deleteDislikeReview(int reviewId, int userId) {
		usersGradeReviewsDbStorage.deleteDislikeReview(reviewId, userId);
	}
}
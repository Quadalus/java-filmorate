package ru.yandex.practicum.filmrate.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmrate.dao.ReviewStorage;
import ru.yandex.practicum.filmrate.exception.NotFoundException;
import ru.yandex.practicum.filmrate.model.Review;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
public class ReviewDbStorage implements ReviewStorage {
	private final JdbcTemplate jdbcTemplate;

	@Autowired
	public ReviewDbStorage(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public Review addReview(Review review) {

		String sqlQuery = "INSERT INTO reviews(content,isPositive,user_id,film_id,useful) " +
				"VALUES (?,?,?,?,?)";
		KeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcTemplate.update(connection -> {
			PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"director_id"});
			stmt.setString(1, review.getContent());
			stmt.setBoolean(2, review.getIsPositive());
			stmt.setInt(3, review.getUserId());
			stmt.setInt(4, review.getFilmId());
			stmt.setInt(5, review.getUseful());
			return stmt;
		}, keyHolder);
		review.setId(Objects.requireNonNull(keyHolder.getKey()).intValue());
		return review;
	}

	@Override
	public Review updateReview(Review review) {
		String sqlQuery = "UPDATE reviews " +
				"SET content=?, isPositive=?, user_id=? ,film_id=?, useful=?" +
				"WHERE review_id = ?";
		int result = jdbcTemplate.update(sqlQuery, review.getContent(), review.getIsPositive(),
				review.getUserId(), review.getFilmId(), review.getUseful());
		if (result != 1) {
			throw new NotFoundException("Невозможно обновить отзыв");
		}
		return review;
	}

	@Override
	public void deleteReview(int reviewId) {
		String sqlQuery = "DELETE FROM reviews " +
				"WHERE review_id = ?";
		jdbcTemplate.update(sqlQuery, reviewId);
	}

	@Override
	public Optional<Review> getReviewById(int reviewId) {
		String sqlQuery = "SELECT * " +
				"FROM reviews " +
				"WHERE review_id = ?";
		List<Review> review = jdbcTemplate.query(sqlQuery, ReviewDbStorage::makeReviews, reviewId);
		if (review.isEmpty()) {
			return Optional.empty();
		}
		return Optional.of(review.get(0));
	}

	@Override
	public List<Review> getReviewsForFilm(int filmId, int count) {
		StringBuilder sqlQuery = new StringBuilder("SELECT * " +
				"FROM reviews " +
				"WHERE 1=1 ");
		if (filmId > 0) {
			sqlQuery.append("AND film_id=? ");
		}
		sqlQuery.append("LIMIT ?");
		return jdbcTemplate.query(sqlQuery.toString(), ReviewDbStorage::makeReviews, filmId, count);
	}

	static Review makeReviews(ResultSet rs, int rowNum) throws SQLException {
		return Review.builder()
				.id(rs.getInt("review_id"))
				.content(rs.getString("content"))
				.isPositive(rs.getBoolean("isPositive"))
				.userId(rs.getInt("user_id"))
				.filmId(rs.getInt("film_id"))
				.useful(rs.getInt("useful")).build();
	}
}
package ru.yandex.practicum.filmrate.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmrate.dao.UsersGradeReviewsStorage;
import ru.yandex.practicum.filmrate.model.UsersGradeReviews;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
public class UsersGradeReviewsDbStorage implements UsersGradeReviewsStorage {
	private final JdbcTemplate jdbcTemplate;

	@Autowired
	public UsersGradeReviewsDbStorage(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public void addLikeReview(int reviewId, int userId) {
		gradeReview(reviewId, userId, 1);
	}

	@Override
	public void addDislikeReview(int reviewId, int userId) {
		gradeReview(reviewId, userId, -1);
	}

	/**
	 * Добавляет в бд запись об оценке отзыва пользователем
	 *
	 * @param reviewId - отзыв, который оценил пользователь
	 * @param userId   - пользователь, который ставит оценку
	 * @param like     - зависит от типа оценки: -1 поставил дизлайк, 1- поставил лайк
	 */
	private void gradeReview(int reviewId, int userId, int like) {
		String sqlQuery = "INSERT INTO users_grade_reviews(user_id, review_id,grade) " +
				"VALUES (?, ?, ?)";
		jdbcTemplate.update(con -> {
			PreparedStatement stmt = con.prepareStatement(sqlQuery);
			stmt.setInt(1, reviewId);
			stmt.setInt(2, userId);
			stmt.setInt(3, like);
			return stmt;
		});
		updateUsefulForReview(reviewId, like);
	}

	/**
	 * обновляет полезность отзыва после изменения данных в таблице users_grade_reviews
	 *
	 * @param reviewId          - отзыв
	 * @param differenceInGrade - разница на которую нужно обновить оценку
	 */
	private void updateUsefulForReview(int reviewId, int differenceInGrade) {
		jdbcTemplate.update("UPDATE reviews " +
				"SET useful = useful+ ? " +
				"WHERE review_id = ?", differenceInGrade, reviewId);
	}

	@Override
	public void deleteLikeReview(int reviewId, int userId) {
		deleteGradeReview(reviewId, userId);
	}

	@Override
	public void deleteDislikeReview(int reviewId, int userId) {
		deleteGradeReview(reviewId, userId);
	}

	/**
	 * Удалить из бд запись об оценке отзыва пользователем
	 *
	 * @param reviewId - отзыв, который оценил пользователь
	 * @param userId   - пользователь, который ставит оценку
	 */
	private void deleteGradeReview(int reviewId, int userId) {
		String sqlQuery = "SELECT * " +
				"FROM users_grade_reviews " +
				"WHERE (review_id = ?) AND (user_id= ?)";
		UsersGradeReviews usersGradeReviews = jdbcTemplate.queryForObject(sqlQuery,
				UsersGradeReviewsDbStorage::makeUsersGradeReviews, reviewId, userId);

		jdbcTemplate.update("DELETE FROM users_grade_reviews " +
				"WHERE (review_id = ?) AND (user_id= ?)", reviewId, userId);

		updateUsefulForReview(reviewId, usersGradeReviews.getGrade());

	}

	static UsersGradeReviews makeUsersGradeReviews(ResultSet rs, int rowNum) throws SQLException {
		return UsersGradeReviews.builder()
				.userId(rs.getInt("user_id"))
				.review_id(rs.getInt("review_id"))
				.grade(rs.getInt("grade")).build();
	}
}
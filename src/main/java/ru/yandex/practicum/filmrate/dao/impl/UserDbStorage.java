package ru.yandex.practicum.filmrate.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmrate.dao.UserStorage;
import ru.yandex.practicum.filmrate.exception.NotFoundException;
import ru.yandex.practicum.filmrate.model.User;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Repository("userDb")
public class UserDbStorage implements UserStorage {
	private final JdbcTemplate jdbcTemplate;

	@Autowired
	public UserDbStorage(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public User addUser(User user) {
		String sqlQuery = "INSERT INTO users(email, login, user_name, birthday) " +
				"VALUES (?, ?, ?, ?)";
		KeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcTemplate.update(connection -> {
			PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"user_id"});
			stmt.setString(1, user.getEmail());
			stmt.setString(2, user.getLogin());
			stmt.setString(3, user.getName());
			stmt.setDate(4, Date.valueOf(user.getBirthday()));
			return stmt;
		}, keyHolder);
		user.setId(Objects.requireNonNull(keyHolder.getKey()).intValue());
		return user;
	}

	@Override
	public User updateUser(User user) {
		String sqlQuery = "UPDATE users SET " +
				"email = ?, login = ?, user_name = ?, birthday = ? " +
				"WHERE user_id = ?";
		int result = jdbcTemplate.update(sqlQuery
				, user.getEmail()
				, user.getLogin()
				, user.getName()
				, Date.valueOf(user.getBirthday())
				, user.getId());
		if (result != 1) {
			throw new NotFoundException("Невозможно обновить пользователя");
		}
		return user;
	}

	@Override
	public void deleteUser(int id) {
		String sqlQuery = "DELETE FROM users " +
				"WHERE user_id = ?";
		jdbcTemplate.update(sqlQuery, id);
	}

	@Override
	public void deleteAllUser() {
		String sqlQuery = "DELETE FROM users";
		jdbcTemplate.update(sqlQuery);
	}

	@Override
	public Optional<User> getUserById(int id) {
		String sqlQuery = "SELECT user_id, email, login, user_name, birthday " +
				"FROM users " +
				"WHERE user_id = ?";
		List<User> users = jdbcTemplate.query(sqlQuery, UserDbStorage::makeUser, id);

		if (users.isEmpty()) {
			return Optional.empty();
		}
		return Optional.of(users.get(0));
	}

	@Override
	public List<User> getAllUsers() {
		String sqlQuery = "SELECT * FROM users";
		return jdbcTemplate.query(sqlQuery, UserDbStorage::makeUser);
	}

	@Override
	public Map<Integer, User> getUserMap() {
		List<User> users = getAllUsers();
		Map<Integer, User> userMap = new HashMap<>();

		for (User user : users) {
			userMap.put(user.getId(), user);
		}
		return userMap;
	}

	private static User makeUser(ResultSet rs, int rowNum) throws SQLException {
		return new User(rs.getInt("user_id"),
				rs.getString("email"),
				rs.getString("login"),
				rs.getString("user_name"),
				rs.getDate("birthday").toLocalDate());
	}
}

package ru.yandex.practicum.filmrate.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmrate.dao.FriendStorage;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Repository
public class FriendsDbStorage implements FriendStorage {
	private final JdbcTemplate jdbcTemplate;

	@Autowired
	public FriendsDbStorage(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public void addFriend(int userId, int friendId) {
		String sqlQuery = "INSERT INTO users_friends(user_id, friend_id) " +
				"VALUES(?, ?)";
		jdbcTemplate.update(con -> {
			PreparedStatement stmt = con.prepareStatement(sqlQuery);
			stmt.setInt(1, userId);
			stmt.setInt(2, friendId);
			return stmt;
		});
	}

	@Override
	public void deleteFriend(int userId, int friendId) {
		String sqlQuery = "DELETE FROM users_friends " +
				"WHERE user_id = ? " +
				"AND friend_id = ?";
		jdbcTemplate.update(sqlQuery, userId, friendId);
	}

	@Override
	public Set<Integer> getFriends(int userId) {
		String sqlQuery = "SELECT * FROM users, users_friends " +
				"WHERE users.user_id = users_friends.friend_id " +
				"AND users_friends.user_id = ?";
		return new HashSet<>(jdbcTemplate.query(sqlQuery, FriendsDbStorage::makeFriendId, userId));
	}

	@Override
	public List<Integer> getMutualFriends(int userId, int friendId) {
		var friends = getFriends(userId);
		var otherFriends = getFriends(friendId);

		return friends.stream()
				.filter(otherFriends::contains)
				.collect(Collectors.toList());
	}

	private static Integer makeFriendId(ResultSet rs, int rowNum) throws SQLException {
		return rs.getInt("friend_id");
	}
}

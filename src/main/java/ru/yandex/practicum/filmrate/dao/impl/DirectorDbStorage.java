package ru.yandex.practicum.filmrate.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmrate.dao.DirectorStorage;
import ru.yandex.practicum.filmrate.exception.NotFoundException;
import ru.yandex.practicum.filmrate.model.Director;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
public class DirectorDbStorage implements DirectorStorage {

	private final JdbcTemplate jdbcTemplate;

	@Autowired
	public DirectorDbStorage(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public Optional<Director> getById(int id) {
		String sqlQuery = "SELECT * " +
				"FROM directors " +
				"WHERE director_id = ?";
		List<Director> directors = jdbcTemplate.query(sqlQuery, DirectorDbStorage::makeDirectors, id);
		if (directors.isEmpty()) {
			return Optional.empty();
		}
		return Optional.of(directors.get(0));
	}

	@Override
	public List<Director> getAllDirectors() {
		String sqlQuery = "SELECT * " +
				"FROM directors";
		return jdbcTemplate.query(sqlQuery, DirectorDbStorage::makeDirectors);
	}

	@Override
	public Director createDirector(Director director) {
		String sqlQuery = "INSERT INTO directors(director_name) " +
				"VALUES (?)";
		KeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcTemplate.update(connection -> {
			PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"director_id"});
			stmt.setString(1, director.getName());
			return stmt;
		}, keyHolder);
		director.setId(Objects.requireNonNull(keyHolder.getKey()).intValue());
		return director;
	}

	@Override
	public Director updateDirector(Director director) {
		String sqlQuery = "UPDATE directors " +
				"SET director_name=? " +
				"WHERE director_id = ?";
		int result = jdbcTemplate.update(sqlQuery
				, director.getName(), director.getId());
		if (result != 1) {
			throw new NotFoundException("Невозможно обновить режиссера");
		}
		return director;
	}

	@Override
	public void deleteDirector(int id) {
		String sqlQuery = "DELETE FROM directors " +
				"WHERE director_id = ?";
		jdbcTemplate.update(sqlQuery, id);
	}

	static Director makeDirectors(ResultSet rs, int rowNum) throws SQLException {
		return new Director(rs.getInt("director_id"),
				rs.getString("director_name"));
	}

	static Director makeDirector(ResultSet rs) throws SQLException {
		return new Director(rs.getInt("director_id"),
				rs.getString("director_name"));
	}
}
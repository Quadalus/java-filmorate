package ru.yandex.practicum.filmrate.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmrate.dao.GenreStorage;
import ru.yandex.practicum.filmrate.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class GenreDbStorage implements GenreStorage {
	private final JdbcTemplate jdbcTemplate;

	@Autowired
	public GenreDbStorage(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public Optional<Genre> getById(int id) {
		String sqlQuery = "SELECT genre_id, genre_name FROM genres " +
				"WHERE genre_id = ?";
		List<Genre> genres = jdbcTemplate.query(sqlQuery, GenreDbStorage::makeGenre, id);

		if (genres.isEmpty()) {
			return Optional.empty();
		}
		return Optional.of(genres.get(0));
	}

	@Override
	public List<Genre> getAllGenres() {
		String sqlQuery = "SELECT genre_id, genre_name FROM genres";
		return jdbcTemplate.query(sqlQuery, GenreDbStorage::makeGenre);
	}

	static Genre makeGenre(ResultSet rs, int rowNum) throws SQLException {
		return new Genre(rs.getInt("genre_id"),
				rs.getString("genre_name"));
	}

	static Genre makeGenreForFilm(ResultSet rs) throws SQLException {
		return new Genre(rs.getInt("genre_id"),
				rs.getString("genre_name"));
	}
}

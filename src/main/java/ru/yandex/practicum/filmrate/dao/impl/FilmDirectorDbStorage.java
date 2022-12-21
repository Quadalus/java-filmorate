package ru.yandex.practicum.filmrate.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmrate.dao.FilmDirectorStorage;
import ru.yandex.practicum.filmrate.model.Director;
import ru.yandex.practicum.filmrate.model.Film;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Repository
public class FilmDirectorDbStorage implements FilmDirectorStorage {
	private final JdbcTemplate jdbcTemplate;

	@Autowired
	public FilmDirectorDbStorage(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public Set<Director> getDirectorsByFilmId(Integer id) {
		String sqlQuery = "SELECT * " +
				"FROM directors AS d " +
				"JOIN directors_film AS df " +
				"ON d.director_id = df.director_id " +
				"WHERE film_id = ?";
		return new LinkedHashSet<>(jdbcTemplate.query(sqlQuery, DirectorDbStorage::makeDirectors, id));
	}

	/**
	 * Сортирует список фильмов конкретного  режиссера по одному из фильтров
	 *
	 * @param idDirector  -id режиссера
	 * @param isSortYear  - сортировка по году выпуска фильма (сначала новые)
	 * @param isSortLikes - сортировка по количеству лайков (по убыванию)
	 * @return - отсортированнный по одному из параметров список фильмов конкретного режиссера
	 */
	@Override
	public List<Film> getSortFilms(int idDirector, boolean isSortYear, boolean isSortLikes) {
		String sqlQuery = "SELECT * " +
				"FROM films AS f " +
				"JOIN mpa_ratings AS mr " +
				"ON f.mpa_id = mr.mpa_id " +
				"WHERE f.film_id IN " +
				"(SELECT df.film_id " +
				"FROM directors_film AS df " +
				"WHERE df.director_id=?) " +
				"ORDER BY ";
		if (isSortYear) {
			sqlQuery += "f.releaseDate ASC ";
		} else if (isSortLikes) {
			sqlQuery += "f.rate DESC ";
		}
		return jdbcTemplate.query(sqlQuery, FilmDbStorage::makeFilm, idDirector);
	}

	@Override
	public void addDirectorsToFilm(int filmId, Set<Director> directors) {
		String sqlQuery = "INSERT INTO directors_film(film_id, director_id) " +
				"VALUES(?, ?)";
		List<Director> directorNoRepeat = directors.stream().distinct().collect(Collectors.toList());
		deleteDirectorsByFilmId(filmId);
		jdbcTemplate.batchUpdate(sqlQuery, new BatchPreparedStatementSetter() {
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				ps.setInt(1, filmId);
				ps.setInt(2, directorNoRepeat.get(i).getId());
			}

			public int getBatchSize() {
				return directorNoRepeat.size();
			}
		});
	}

	@Override
	public void deleteDirectorsByFilmId(int filmId) {
		String sqlQuery = "DELETE FROM directors_film " +
				"WHERE film_id = ?";
		jdbcTemplate.update(sqlQuery, filmId);
	}
}
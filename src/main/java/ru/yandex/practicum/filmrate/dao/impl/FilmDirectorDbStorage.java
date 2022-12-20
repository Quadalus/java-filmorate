package ru.yandex.practicum.filmrate.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmrate.dao.FilmDirectorStorage;
import ru.yandex.practicum.filmrate.model.Film;

import java.util.List;

@Repository
public class FilmDirectorDbStorage implements FilmDirectorStorage {
	private final JdbcTemplate jdbcTemplate;
	private final FilmDbStorage filmDbStorage;

	@Autowired
	public FilmDirectorDbStorage(JdbcTemplate jdbcTemplate, FilmDbStorage filmDbStorage) {
		this.jdbcTemplate = jdbcTemplate;
		this.filmDbStorage = filmDbStorage;
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
			sqlQuery += "f.releaseDate DESC ";
		} else if (isSortLikes) {
			sqlQuery += "f.rate DESC ";
		}
		List<Film> films = jdbcTemplate.query(sqlQuery, FilmDbStorage::makeFilm, idDirector);
		filmDbStorage.addGenresToFilms(films);
		return films;
	}
}
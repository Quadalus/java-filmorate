package ru.yandex.practicum.filmrate.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmrate.dao.FilmStorage;
import ru.yandex.practicum.filmrate.exception.NotFoundException;
import ru.yandex.practicum.filmrate.model.Film;
import ru.yandex.practicum.filmrate.model.Mpa;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.function.UnaryOperator.identity;
import static ru.yandex.practicum.filmrate.dao.impl.DirectorDbStorage.makeDirector;
import static ru.yandex.practicum.filmrate.dao.impl.GenreDbStorage.makeGenreForFilm;

@Repository
public class FilmDbStorage implements FilmStorage {
	private final JdbcTemplate jdbcTemplate;

	@Autowired
	public FilmDbStorage(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public Film addFilm(Film film) {
		String sqlQuery = "INSERT INTO films(name, description, releasedate, DURATION, rate, mpa_id) " +
				"VAlUES (?, ?, ?, ?, ?, ?)";
		KeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcTemplate.update(connection -> {
			PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"film_id"});
			stmt.setString(1, film.getName());
			stmt.setString(2, film.getDescription());
			stmt.setDate(3, Date.valueOf(film.getReleaseDate()));
			stmt.setInt(4, film.getDuration());
			stmt.setInt(5, (film.getRate() == null) ? 0 : film.getRate());
			stmt.setInt(6, film.getMpa().getId());
			return stmt;
		}, keyHolder);
		film.setId(Objects.requireNonNull(keyHolder.getKey()).intValue());
		return film;
	}

	@Override
	public Film updateFilm(Film film) {
		String sqlQuery = "UPDATE films SET " +
				"name = ?, description = ?, releasedate = ?, duration = ?, rate = ?, mpa_id = ? " +
				"WHERE film_id = ?";
		int result = jdbcTemplate.update(sqlQuery
				, film.getName()
				, film.getDescription()
				, Date.valueOf(film.getReleaseDate())
				, film.getDuration()
				, (film.getRate() == null) ? 0 : film.getRate()
				, film.getMpa().getId()
				, film.getId());

		if (result != 1) {
			throw new NotFoundException("Фильм не найден.");
		}
		return film;
	}

	@Override
	public void deleteFilm(int id) {
		String sqlQuery = "DELETE FROM films " +
				"WHERE film_id = ?";
		jdbcTemplate.update(sqlQuery, id);
	}

	@Override
	public void deleteAllFilms() {
		String sqlQuery = "DELETE FROM films";
		jdbcTemplate.update(sqlQuery);
	}

	@Override
	public Optional<Film> getFilmById(int id) {
		String sqlQuery = "SELECT * FROM films AS f " +
				"JOIN mpa_ratings AS mr ON f.mpa_id = mr.mpa_id " +
				"WHERE film_id = ?";
		List<Film> films = jdbcTemplate.query(sqlQuery, FilmDbStorage::makeFilm, id);

		if (films.isEmpty()) {
			return Optional.empty();
		}
		return Optional.of(films.get(0));
	}

	@Override
	public List<Film> getListFilms() {
		String sqlQuery = "SELECT * FROM films AS f " +
				"JOIN mpa_ratings AS mr ON f.mpa_id = mr.mpa_id ";
		List<Film> films = jdbcTemplate.query(sqlQuery, FilmDbStorage::makeFilm);
		addGenresToFilms(films);
		addDirectorsToFilms(films);
		return films;
	}

    @Override
    public List<Film> getRecommendedFilms(int userId) {
		String sqlQuery = "SELECT * FROM likes l " +
				"  JOIN films f ON f.film_id = l.film_id " +
				"  JOIN mpa_ratings mr ON f.mpa_id = mr.mpa_id " +
				"WHERE user_id = (" +
				"    SELECT user_id FROM likes " +
				"    WHERE film_id IN (" +
				"        SELECT film_id FROM likes " +
				"        WHERE user_id = ?) " +
				"        AND user_id <> ? " +
				"    GROUP BY user_id " +
				"    ORDER BY COUNT(film_id) DESC " +
				"    LIMIT 1) " +
				"AND l.film_id NOT IN (" +
				"    SELECT film_id FROM likes " +
				"    WHERE user_id = ?);";
        List<Film> films = jdbcTemplate.query(sqlQuery, FilmDbStorage::makeFilm, userId, userId, userId);
        addGenresToFilms(films);
        return films;
    }

	@Override
	public Map<Integer, Film> getFilmMap() {
		List<Film> films = getListFilms();
		Map<Integer, Film> filmMap = new HashMap<>();

		for (Film film : films) {
			filmMap.put(film.getId(), film);
		}
		return filmMap;
	}

	public List<Film> searchFilm(String query, String searchParameters) {
		String sqlQuery = sqlQueryBySearchParameters(searchParameters);
		String searchQuery = "%" + query.toLowerCase() + "%";
		List<Film> films = jdbcTemplate.query(sqlQuery, FilmDbStorage::makeFilm, searchQuery);
        addGenresToFilms(films);
        addDirectorsToFilms(films);
        return films;
    }

	@Override
	public List<Film> getCommonFilms(int userId, int friendId) {
		String sqlQuery = "SELECT f.*, mr.mpa_name " +
				"FROM likes AS l " +
				"JOIN films f ON f.film_id = l.film_id " +
				"JOIN MPA_RATINGS mr ON f.mpa_id = mr.mpa_id " +
				"WHERE l.user_id = ? " +
				"OR l.user_id = ? " +
				"GROUP BY l.film_id " +
				"HAVING COUNT(l.film_id) > 1 ";
		List<Film> films = jdbcTemplate.query(sqlQuery, FilmDbStorage::makeFilm, userId, friendId);
		addGenresToFilms(films);
		addDirectorsToFilms(films);
		return films;
	}

	private static String sqlQueryBySearchParameters(String searchParameters) {
		String[] param = searchParameters.split(",");
		String sqlQuery = "";

		if (param.length == 1 && param[0].equals("title")) {
			sqlQuery = "SELECT f.film_id, f.name, f.description, f.releasedate, f.duration, f.rate, f.mpa_id, mr.mpa_name " +
                    "FROM films AS f " +
                    "JOIN mpa_ratings AS mr ON f.mpa_id = mr.mpa_id " +
					"WHERE LOWER(f.name) LIKE ? " +
					"ORDER BY rate";
		}

		if (param.length == 1 && param[0].equals("director")) {
			sqlQuery = "SELECT f.film_id, f.name, f.description, f.releasedate, f.duration, f.rate, f.mpa_id, d.director_name, mr.mpa_name " +
                    "FROM films AS f " +
					"JOIN directors_film df ON f.film_id = df.film_id " +
					"JOIN directors d ON d.director_id = df.director_id " +
                    "JOIN mpa_ratings AS mr ON f.mpa_id = mr.mpa_id " +
					"WHERE LOWER(d.director_name) LIKE ? " +
					"ORDER BY rate";
		}

		if (param.length == 2) {
			sqlQuery = "SELECT f.film_id, f.name, f.description, f.releasedate, f.duration, f.rate, f.mpa_id, d.director_name, mr.mpa_name " +
                    "FROM films AS f " +
					"LEFT JOIN directors_film df ON f.film_id = df.film_id " +
					"LEFT JOIN directors d ON d.director_id = df.director_id " +
					"LEFT JOIN mpa_ratings AS mr ON f.mpa_id = mr.mpa_id " +
					"WHERE LOWER(CONCAT(f.name, d.director_name)) LIKE ? " +
					"ORDER BY rate";
		}
		return sqlQuery;
	}

	static Film makeFilm(ResultSet rs, int rowNum) throws SQLException {
		return new Film(rs.getInt("film_id"),
				rs.getString("name"),
				rs.getString("description"),
				rs.getDate("releasedate").toLocalDate(),
				rs.getInt("duration"),
				rs.getInt("rate"),
				new Mpa(rs.getInt("mpa_id"), rs.getString("mpa_name")));
	}

	public void addGenresToFilms(List<Film> films) {
		String filmIds = films.stream()
				.map(Film::getId)
				.map(String::valueOf)
				.collect(Collectors.joining(","));
		final Map<Integer, Film> filmById = films.stream().collect(Collectors.toMap(Film::getId, identity()));
		final String sqlQuery = "SELECT * FROM genres AS g, films_genres AS fg " +
				"WHERE fg.genre_id = g.genre_id " +
				"AND fg.film_id IN (" + filmIds + ")";
		jdbcTemplate.query(sqlQuery, (rs) -> {
			final Film film = filmById.get(rs.getInt("film_id"));
			film.setGenreToFilm(makeGenreForFilm(rs));
		});
	}

	public void addDirectorsToFilms(List<Film> films) {
		String filmIds = films.stream()
				.map(Film::getId)
				.map(String::valueOf)
				.collect(Collectors.joining(","));
		final Map<Integer, Film> filmById = films.stream().collect(Collectors.toMap(Film::getId, identity()));
		final String sqlQuery = "SELECT * " +
				"FROM directors AS g, directors_film AS df " +
				"WHERE df.director_id=g.director_id " +
				"AND df.film_id IN (" + filmIds + ")";
		jdbcTemplate.query(sqlQuery, (rs) -> {
			final Film film = filmById.get(rs.getInt("film_id"));
			film.setDirectorsToFilm(makeDirector(rs));
		});
	}
}

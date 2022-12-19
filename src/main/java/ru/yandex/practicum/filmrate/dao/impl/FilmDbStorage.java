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

    static Film makeFilm(ResultSet rs, int rowNum) throws SQLException {
        return new Film(rs.getInt("film_id"),
                rs.getString("name"),
                rs.getString("description"),
                rs.getDate("releasedate").toLocalDate(),
                rs.getInt("duration"),
                rs.getInt("rate"),
                new Mpa(rs.getInt("mpa_id"), rs.getString("mpa_name")));
    }

    private void addGenresToFilms(List<Film> films) {
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
}

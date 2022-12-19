package ru.yandex.practicum.filmrate.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmrate.dao.LikeStorage;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class LikeDbStorage implements LikeStorage {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public LikeDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void addLike(int filmId, int userId) {
        String sqlQuery = "INSERT INTO likes(film_id, user_id) " +
                "VALUES (?, ?)";
        jdbcTemplate.update(con -> {
            PreparedStatement stmt = con.prepareStatement(sqlQuery);
            stmt.setInt(1, filmId);
            stmt.setInt(2, userId);
            return stmt;
        });
        updateRate(filmId);
    }

    @Override
    public void deleteLike(int filmId, int userId) {
        String sqlQuery = "DELETE FROM likes " +
                "WHERE film_id = ? " +
                "AND user_id = ?";
        jdbcTemplate.update(sqlQuery, filmId, userId);
        updateRate(filmId);
    }

    @Override
    public List<Integer> getTopFilms(int count) {
        String sqlQuery = "SELECT * FROM films AS f " +
                "LEFT JOIN MPA_RATINGS AS m ON m.mpa_id = f.FILM_ID " +
                "LEFT JOIN likes l on f.film_id = l.film_id " +
                "GROUP BY f.film_id " +
                "ORDER BY COUNT(l.film_id) " +
                "DESC LIMIT ?;";
        return jdbcTemplate.query(sqlQuery, LikeDbStorage::makeLike, count);
    }

    private void updateRate(int filmId) {
        jdbcTemplate.update("UPDATE films AS f " +
                "SET rate = (" +
                "SELECT COUNT(l.user_id) " +
                "FROM likes AS l " +
                "WHERE l.film_id = f.film_id" +
                ") " +
                "WHERE film_id = ?", filmId);
    }

    private static Integer makeLike(ResultSet rs, int rowNum) throws SQLException {
        return rs.getInt("film_id");
    }
}

package ru.yandex.practicum.filmrate.dao.impl.InDbImpl;

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
        String sqlQuery = "insert into LIKES(FILM_ID, USER_ID) " +
                "values (?, ?)";
        jdbcTemplate.update(con -> {
            PreparedStatement stmt = con.prepareStatement(sqlQuery);
            stmt.setInt(1, filmId);
            stmt.setInt(2, userId);
            return stmt;
        });
    }

    @Override
    public void deleteLike(int filmId, int userId) {
        String sqlQuery = "delete from LIKES " +
                "where FILM_ID = ? " +
                "and USER_ID = ?";
        jdbcTemplate.update(sqlQuery);
    }

    @Override
    public List<Integer> getTopFilms(int count) {
        String sqlQuery = "SELECT * FROM films AS f " +
                "LEFT JOIN MPA_RATINGS m ON m.mpa_id = f.FILM_ID " +
                "LEFT OUTER JOIN likes l on f.film_id = l.film_id " +
                "GROUP BY f.film_id " +
                "ORDER BY COUNT(l.film_id) " +
                "DESC LIMIT ?;";
        return jdbcTemplate.query(sqlQuery, LikeDbStorage::makeLike, count);
    }

    private static Integer makeLike(ResultSet rs, int rowNum) throws SQLException {
        return rs.getInt("FILM_ID");
    }
}

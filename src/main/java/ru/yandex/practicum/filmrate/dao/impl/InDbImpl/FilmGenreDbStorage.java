package ru.yandex.practicum.filmrate.dao.impl.InDbImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmrate.dao.FilmGenreStorage;
import ru.yandex.practicum.filmrate.model.FilmGenre;
import ru.yandex.practicum.filmrate.model.Genre;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class FilmGenreDbStorage implements FilmGenreStorage {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public FilmGenreDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Genre> getGenresByFilmId(int filmId) {
        String sqlQuery = "select FILM_ID, GENRE_ID from FILMS_GENRES " +
                "where FILM_ID = ?";
        jdbcTemplate.query(sqlQuery, FilmGenreDbStorage::makeFilmGenre);
        return null;
    }

    @Override
    public void addGenreToFilm(int filmId, List<Integer> genreList) {
        String sqlQuery = "insert into FILMS_GENRES(film_id, genre_id) " +
                "values (?, ?)";
        jdbcTemplate.update(con -> {
            PreparedStatement stmt = con.prepareStatement(sqlQuery);
            for (Integer ids : genreList) {
                stmt.setInt(filmId, ids);
            }
            stmt.executeBatch();
            return stmt;
        });
    }

    @Override
    public void deleteGenresByFilmId(int filmId) {
        String sqlQuery = "delete from FILMS_GENRES " +
                "where FILM_ID = ?";
        jdbcTemplate.update(sqlQuery);
    }

    private static FilmGenre makeFilmGenre(ResultSet rs, int rowNum) throws SQLException {
        return new FilmGenre(rs.getInt("FILM_ID"),
                rs.getInt("GENRE_ID"));
    }
}

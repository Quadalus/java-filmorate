package ru.yandex.practicum.filmrate.dao.impl.InDbImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmrate.dao.FilmGenreStorage;
import ru.yandex.practicum.filmrate.model.Genre;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Repository
public class FilmGenreDbStorage implements FilmGenreStorage {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public FilmGenreDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Set<Genre> getGenresByFilmId(int filmId) {
        String sqlQuery = "SELECT * FROM genres g " +
                "JOIN films_genres fg on g.genre_id = fg.GENRE_ID " +
                "WHERE film_id = ?";
        return new LinkedHashSet<>(jdbcTemplate.query(sqlQuery, GenreDbStorage::makeGenre, filmId));
    }

    @Override
    public void addGenreToFilm(int filmId, Set<Genre> genreList) {
        String sqlQuery = "insert into FILMS_GENRES(film_id, genre_id) " +
                "values (?, ?)";
        List<Genre> genresNoRepeat = genreList.stream().distinct().collect(Collectors.toList());
        deleteGenresByFilmId(filmId);
        jdbcTemplate.batchUpdate(sqlQuery, new BatchPreparedStatementSetter() {
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        ps.setInt(1, filmId);
                        ps.setInt(2, genresNoRepeat.get(i).getId());
                    }
                    public int getBatchSize() {
                        return genresNoRepeat.size();
                    }
                });
    }

    @Override
    public void deleteGenresByFilmId(int filmId) {
        String sqlQuery = "delete from FILMS_GENRES " +
                "where FILM_ID = ?";
        jdbcTemplate.update(sqlQuery, filmId);
    }
}

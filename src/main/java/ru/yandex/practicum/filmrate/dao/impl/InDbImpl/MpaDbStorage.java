package ru.yandex.practicum.filmrate.dao.impl.InDbImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmrate.dao.MpaStorage;
import ru.yandex.practicum.filmrate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class MpaDbStorage implements MpaStorage {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public MpaDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<Mpa> getMpaById(int id) {
        String sqlQuery = "select MPA_ID, MPA_NAME " +
                "from MPA_RATINGS " +
                "where MPA_ID = ?";

        List<Mpa> mpaList = jdbcTemplate.query(sqlQuery, MpaDbStorage::makeMpa, id);
        if (mpaList.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(mpaList.get(0));
    }

    @Override
    public List<Mpa> getAllMpa() {
        String sqlQuery = "select MPA_ID, MPA_NAME " +
                "from MPA_RATINGS ";

        return jdbcTemplate.query(sqlQuery, MpaDbStorage::makeMpa);
    }

    private static Mpa makeMpa(ResultSet rs, int rowNum) throws SQLException {
        return new Mpa(rs.getInt("MPA_ID"),
                rs.getString("MPA_NAME"));
    }
}

package ru.yandex.practicum.filmorate.storage.Mpa;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.MpaNotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
public class MpaDbStorage implements MpaStorage {
    private Logger log = LoggerFactory.getLogger(MpaDbStorage.class);
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public MpaDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Mpa> findAllMpa() {
        List<Mpa> result = new ArrayList<>();
        String sql = "SELECT MPA_ID, MPA_NAME FROM MPA";
        result = jdbcTemplate.query(sql, this::mapRowToMpa);
        return result;
    }

    @Override
    public Mpa findMpa(int id) {
        String sql = "SELECT MPA_ID, MPA_NAME FROM MPA WHERE MPA_ID = ?";
        try {
            return jdbcTemplate.queryForObject(sql, this::mapRowToMpa, id);
        } catch (EmptyResultDataAccessException e) {
            log.debug("Mpa не найден");
            throw new MpaNotFoundException("Mpa не найден");
        }
    }

    private Mpa mapRowToMpa(ResultSet resultSet, int rowNum) throws SQLException {
        return Mpa.builder()
                .id(resultSet.getInt("MPA_ID"))
                .name(resultSet.getString("MPA_NAME"))
                .build();
    }
}
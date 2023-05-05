package ru.yandex.practicum.filmorate.storage.genre;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
public class GenreDbStorage implements GenreStorage {
    private JdbcTemplate jdbcTemplate;
    private Logger log = LoggerFactory.getLogger(GenreDbStorage.class);

    @Autowired
    public GenreDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Genre> findAllGenres() {
        List<Genre> result = new ArrayList<>();
        String sql = "SELECT * FROM GENRES";
        result.addAll(jdbcTemplate.query(sql, this::mapRowToGenre));
        return result;
    }

    @Override
    public Genre findGenre(int id) {
        String sql = "SELECT * FROM GENRES WHERE GENRE_ID = ?";
        try {
            Genre genre = jdbcTemplate.queryForObject(sql, this::mapRowToGenre, id);
            return genre;
        } catch (EmptyResultDataAccessException e) {
            log.debug("Genre не найден");
            throw new NotFoundException("Genre не найден");
        }
    }

    private Genre mapRowToGenre(ResultSet resultSet, int rowNum) throws SQLException {
        return Genre.builder()
                .id(resultSet.getInt("GENRE_ID"))
                .name(resultSet.getString("GENRE_NAME"))
                .build();
    }
}
package ru.yandex.practicum.filmorate.storage.like;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class LikeDbStorage implements LikeStorage {
    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    public LikeDbStorage(JdbcTemplate jdbcTemplate,
                         NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    public Long addLike(Long idFilm, Long idUser) {
        String sql = "INSERT INTO LIKES_LIST(FILM_ID, USER_ID)" + "VALUES(?, ?)";
        jdbcTemplate.update(sql, idFilm, idUser);
        return idFilm;
    }

    @Override
    public Long deleteLike(Long idFilm, Long idUser) {
        String sql = "DELETE FROM LIKES_LIST WHERE FILM_ID = ? AND USER_ID = ?";
        jdbcTemplate.update(sql, idFilm, idUser);
        return idFilm;
    }
}
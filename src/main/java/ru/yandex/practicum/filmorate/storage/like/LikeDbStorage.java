package ru.yandex.practicum.filmorate.storage.like;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class LikeDbStorage implements LikeStorage {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public LikeDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
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

    @Override
    public List<Long> findTop(int count) {
        String sql = "SELECT f.FILM_ID FROM FILMS AS f " +
                "LEFT JOIN LIKES_LIST AS ll ON f.FILM_ID = ll.FILM_ID GROUP BY f.FILM_ID " +
                "ORDER BY COUNT(ll.USER_ID) desc LIMIT ?";
        ArrayList<Long> idFilms = new ArrayList<>();
        idFilms.addAll(jdbcTemplate.queryForList(sql, Long.class, count));
        return idFilms;
    }
}
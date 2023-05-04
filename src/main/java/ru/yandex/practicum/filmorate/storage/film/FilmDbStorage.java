package ru.yandex.practicum.filmorate.storage.film;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;

@Component
public class FilmDbStorage implements FilmStorage {
    private final Logger log = LoggerFactory.getLogger(FilmDbStorage.class);
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public FilmDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Film addFilm(Film film) {
        String sql = "INSERT INTO FILMS (FILM_NAME,DESCRIPTION, DURATION," +
                "RELEASE_DATE, MPA_ID)" +
                "Values (?, ?, ?, ?,?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sql, new String[]{"FILM_ID"});
            stmt.setString(1, film.getName());
            stmt.setString(2, film.getDescription());
            stmt.setInt(3, film.getDuration());
            stmt.setDate(4, Date.valueOf(film.getReleaseDate()));
            stmt.setLong(5, film.getMpa().getId());
            return stmt;
        }, keyHolder);

        film.setId(keyHolder.getKey().longValue());

        Film createFilm = film;
        findAndSetNameMPA(createFilm);

        if (film.getGenres() != null) {
            insertGenreList(createFilm);
            findAndSetGenreListWithName(createFilm);
        }
        return createFilm;
    }

    @Override
    public Film updateFilm(Film film) {
        findFilm(film.getId());

        String sql = "UPDATE FILMS SET FILM_NAME = ?, DESCRIPTION = ?," +
                "DURATION = ?, RELEASE_DATE = ?, MPA_ID = ? WHERE FILM_ID = ?";
        jdbcTemplate.update(sql,
                film.getName(),
                film.getDescription(),
                film.getDuration(),
                film.getReleaseDate(),
                film.getMpa().getId(),
                film.getId());
        Film updateFilm = film;

        if (film.getGenres() != null) {
            String sqlGenres = "DELETE FROM GENRE_LIST WHERE FILM_ID = ?";
            jdbcTemplate.update(sqlGenres,
                    film.getId());
            insertGenreList(updateFilm);
            findAndSetGenreListWithName(film);
        }
        findAndSetNameMPA(updateFilm);
        return updateFilm;
    }

    @Override
    public List<Film> findAllFilms() {
        String sql = "SELECT * FROM FILMS";
        List<Film> allFilms = jdbcTemplate.query(sql, this::mapRowToFilm);
        for (Film film : allFilms) {
            findAndSetNameMPA(film);
            findAndSetGenreListWithName(film);
        }
        return allFilms;
    }

    @Override
    public Film findFilm(Long id) {
        List<Long> films = jdbcTemplate.queryForList("SELECT FILM_ID FROM FILMS", Long.class);
        if (!films.contains(id)) {
            log.info("Фильм с идентификатором {} не найден.", id);
            throw new NotFoundException("Фильм не найден");
        }
        Film findedFilm = jdbcTemplate.queryForObject(
                "SELECT * FROM FILMS WHERE FILM_ID = ?",
                this::mapRowToFilm, id);
        findAndSetNameMPA(findedFilm);
        findAndSetGenreListWithName(findedFilm);
        return findedFilm;
    }

    private Film mapRowToFilm(ResultSet resultSet, int rowNum) throws SQLException {
        return Film.builder()
                .id(resultSet.getLong("FILM_ID"))
                .name(resultSet.getString("FILM_NAME"))
                .releaseDate(resultSet.getDate("release_Date").toLocalDate())
                .description(resultSet.getString("description"))
                .duration(resultSet.getInt("duration"))
                .mpa(new Mpa(resultSet.getInt("MPA_ID")))
                .build();
    }

    private void insertGenreList(Film film) {
        for (Genre genre : film.getGenres()) {
            String sqlGenres = "INSERT INTO GENRE_LIST (FILM_ID, GENRE_ID)" + "Values(?, ?)";
            jdbcTemplate.update(sqlGenres,
                    film.getId(), genre.getId());
        }
    }

    private void findAndSetNameMPA(Film updateFilm) {
        String nameMpa;
        nameMpa = jdbcTemplate.queryForObject("SELECT MPA_NAME FROM MPA WHERE MPA_ID = ?", String.class,
                updateFilm.getMpa().getId());
        updateFilm.getMpa().setName(nameMpa);
    }

    private void findAndSetGenreListWithName(Film film) {
        List<Genre> genres = jdbcTemplate.query(
                "SELECT * FROM GENRES AS g " +
                        "JOIN GENRE_LIST AS gl ON " +
                        "gl.GENRE_ID = g.GENRE_ID " +
                        "WHERE FILM_ID = ? " +
                        "ORDER BY GENRE_ID", this::mapRowToGenre,
                film.getId());
        film.setGenres(new HashSet<>());
        film.getGenres().addAll(genres);
    }

    private Genre mapRowToGenre(ResultSet resultSet, int rowNum) throws SQLException {
        return Genre.builder()
                .id(resultSet.getInt("GENRE_ID"))
                .name(resultSet.getString("GENRE_NAME"))
                .build();
    }
}
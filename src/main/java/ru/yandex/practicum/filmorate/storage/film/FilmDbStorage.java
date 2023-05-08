package ru.yandex.practicum.filmorate.storage.film;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
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
import java.util.*;

@Component
public class FilmDbStorage implements FilmStorage {
    private final Logger log = LoggerFactory.getLogger(FilmDbStorage.class);
    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    public FilmDbStorage(JdbcTemplate jdbcTemplate,
                         NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
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
        } else {
            film.setGenres(new HashSet<>());
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
            deleteGenresIdFromGenreListInFilmId(film.getId());
            insertGenreList(updateFilm);
            findAndSetGenreListWithName(film);
        }
        findAndSetNameMPA(updateFilm);
        return updateFilm;
    }

    @Override
    public List<Film> findAllFilms() {
        List<Film> allFilms = jdbcTemplate.query("SELECT * " +
                "FROM FILMS AS f JOIN MPA M on M.MPA_ID = f.MPA_ID", this::mapRowToFilm);

        List<Long> ids = new ArrayList<>();
        for (int i = 0; i < allFilms.size(); i++) {
            ids.add(allFilms.get(i).getId());
        }

        var sqlQuery = "SELECT * FROM GENRES AS g " +
                "JOIN GENRE_LIST AS gl ON " +
                "gl.GENRE_ID = g.GENRE_ID " +
                "WHERE FILM_ID IN (:ids) " +
                "ORDER BY GENRE_ID";
        var idParams = new MapSqlParameterSource("ids", ids);
        List<Map<String, Object>> resultSet = namedParameterJdbcTemplate.queryForList(sqlQuery, idParams);
        Map<Long, Set<Genre>> filmsGenres = new HashMap<>();
        for (var mapRow : resultSet) {
            long filmId = (Integer) mapRow.get("FILM_ID");
            int genreId = (Integer) mapRow.get("GENRE_ID");
            String genreName = (String) mapRow.get("GENRE_NAME");
            if (!filmsGenres.containsKey(filmId)) {
                filmsGenres.put(filmId, new HashSet<>());
            }
            filmsGenres.get(filmId).add(new Genre(genreId, genreName));
        }
        for (Film film : allFilms) {
            if (filmsGenres.get(film.getId()) == null) {
                Set<Genre> empty = new HashSet<>();
                film.setGenres(empty);
            } else {
                film.setGenres(filmsGenres.get(film.getId()));
            }
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
                "SELECT * FROM FILMS AS f" +
                        " JOIN MPA AS m ON m.MPA_ID = f.MPA_ID WHERE FILM_ID = ?",
                this::mapRowToFilm, id);
        findAndSetGenreListWithName(findedFilm);
        return findedFilm;
    }

    public void deleteAllFilms() {
        String sql = "DELETE FROM GENRE_LIST;ALTER TABLE FILMS ALTER COLUMN FILM_ID RESTART WITH 1;" +
                "DELETE FROM FILMS;";
        jdbcTemplate.update(sql);
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

    private Film mapRowToFilm(ResultSet resultSet, int rowNum) throws SQLException {
        return Film.builder()
                .id(resultSet.getLong("FILM_ID"))
                .name(resultSet.getString("FILM_NAME"))
                .releaseDate(resultSet.getDate("release_Date").toLocalDate())
                .description(resultSet.getString("description"))
                .duration(resultSet.getInt("duration"))
                .mpa(new Mpa(resultSet.getInt("MPA_ID"), resultSet.getString("MPA_NAME")))
                .build();
    }

    private void insertGenreList(Film film) {
        List<Object[]> batch = new ArrayList<>();
        for (Genre genre : film.getGenres()) {
            Object[] values = new Object[]{
                    film.getId(),
                    genre.getId()};
            batch.add(values);
        }
        int[] updateCounts = jdbcTemplate.batchUpdate("INSERT INTO GENRE_LIST (FILM_ID, GENRE_ID) " +
                "Values(?, ?)", batch);
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

    private void deleteGenresIdFromGenreListInFilmId(Long id) {
        String sqlGenres = "DELETE FROM GENRE_LIST WHERE FILM_ID = ?";
        jdbcTemplate.update(sqlGenres, id);
    }
}
package ru.yandex.practicum.filmorate.storage.film;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class FilmDbStorage implements FilmStorage {
    private final Logger log = LoggerFactory.getLogger(UserDbStorage.class);
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
        findAndSetNameMPA(film, createFilm);

        if (film.getGenres() != null) {
            insertGenreList(film);
            findAndSetGenreListWithName(film, createFilm);
        }
        return createFilm;
    }

    @Override
    public Film updateFilm(Film film) {
        Film updateFilm = findFilm(film.getId());//если не находит, выбрасывает NotFoundException

        String sql = "UPDATE FILMS SET FILM_NAME = ?, DESCRIPTION = ?," +
                "DURATION = ?, RELEASE_DATE = ?, MPA_ID = ? WHERE FILM_ID = ?";
        jdbcTemplate.update(sql,
                film.getName(),
                film.getDescription(),
                film.getDuration(),
                film.getReleaseDate(),
                film.getMpa().getId(),
                film.getId());

        if (film.getGenres() != null) {
            String sqlGenres = "DELETE FROM GENRE_LIST WHERE FILM_ID = ?";
            jdbcTemplate.update(sqlGenres,
                    film.getId());
            insertGenreList(film);
            findAndSetGenreListWithName(film, updateFilm);
        }
        findAndSetNameMPA(film, updateFilm);

        return updateFilm;
    }

    @Override
    public List<Film> findAllFilms() {
        String sql = "SELECT * FROM FILMS";
        List<Film> allFilms = jdbcTemplate.query(sql, this::mapRowToFilm);
        for (Film film : allFilms) {
            findAndSetNameMPA(film, film);
            findGenreList(film);
        }
        return allFilms;
    }

    @Override
    public Film findFilm(Long id) {
        List<Long> films = jdbcTemplate.queryForList("SELECT FILM_ID FROM FILMS", Long.class);
        if (!films.contains(id)) {
            //log.info("Пользователь с идентификатором {} не найден.", id);
            throw new NotFoundException("Фильм не найден");
        }
        Film findedFilm = jdbcTemplate.queryForObject(
                "SELECT * FROM FILMS WHERE FILM_ID = ?",
                this::mapRowToFilm, id);
        findAndSetNameMPA(findedFilm, findedFilm);
        findGenreList(findedFilm);
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
        for (int i = 0; i < film.getGenres().size(); i++) {
            String sqlGenres = "INSERT INTO GENRE_LIST (FILM_ID, GENRE_ID)" + "Values(?, ?)";
            jdbcTemplate.update(sqlGenres,
                    film.getId(),
                    film.getGenres().get(i).getId());
        }
    }

    private void findAndSetNameMPA(Film film, Film updateFilm) {
        updateFilm.getMpa().setName(
                jdbcTemplate.queryForObject("SELECT MPA_NAME FROM MPA WHERE MPA_ID = ?", String.class,
                        film.getMpa().getId()));
    }

    private void findAndSetGenreListWithName(Film film, Film updateFilm) {
        ArrayList<Genre> genres = new ArrayList<>();
        for (int i = 0; i < film.getGenres().size(); i++) {
            genres.add(new Genre(film.getGenres().get(i).getId(),
                    jdbcTemplate.queryForObject("SELECT GENRE_NAME FROM GENRES WHERE GENRE_ID = ?",
                            String.class, film.getGenres().get(i).getId())));
        }
        updateFilm.setGenres(genres);
    }

    private void findGenreList(Film film) {
        ArrayList<Genre> genres = new ArrayList<>();
        ArrayList<Integer> idGenres = new ArrayList<>();
        film.setGenres(genres);

        idGenres.addAll(
                jdbcTemplate.queryForList("SELECT GENRE_ID FROM GENRE_LIST WHERE FILM_ID = ?",
                        Integer.class, film.getId()));

        for (Integer id : idGenres) {
            film.getGenres().add(new Genre(id,
                    jdbcTemplate.queryForObject("SELECT GENRE_NAME FROM GENRES WHERE GENRE_ID = ?",
                            String.class, id)));
        }
    }
    private Film mapRowToFilmWithMpa(ResultSet resultSet, int rowNum) throws SQLException {
        return Film.builder()
                .id(resultSet.getLong("FILM_ID"))
                .name(resultSet.getString("FILM_NAME"))
                .releaseDate(resultSet.getDate("release_Date").toLocalDate())
                .description(resultSet.getString("description"))
                .duration(resultSet.getInt("duration"))
                .mpa(new Mpa(resultSet.getInt("MPA_ID"), resultSet.getString("mpa_name")))
                .genres(List.of(new Genre(
                        resultSet.getInt("GENRE_ID"),
                        resultSet.getString("GENRE_NAME"))).stream().collect(Collectors.toList()))
                .build();
    }

    private Mpa mapRowToMpa(ResultSet resultSet, int rowNum) throws SQLException {
        return Mpa.builder()
                .id(resultSet.getInt("MPA_ID"))
                .name(resultSet.getString("MPA_NAME"))
                .build();
    }
}


//return jdbcTemplate.queryForObject(" SELECT *" +
//        "FROM FILMS AS f LEFT JOIN MPA as m ON" +
//        " f.MPA_ID = m.MPA_ID WHERE FILM_ID = ?",//SELECT * FROM FILMS WHERE FILM_ID = ?,
//        this::mapRowToFilmWithMpa, id);

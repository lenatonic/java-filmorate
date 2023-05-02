
package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FilmControllerTest {
    private InMemoryFilmStorage inMemoryFilmStorage = new InMemoryFilmStorage();
    private UserService userService;
    private FilmService filmService = new FilmService(inMemoryFilmStorage, userService);
    private FilmController filmController = new FilmController(filmService);

    @Test
    public void shouldReturnFilmAfterMethodAddFilm() {
        Film testFilm = Film.builder()
                .name("newFilm")
                .description("descFilm")
                .rate(1)
                .duration(120)
                .releaseDate(LocalDate.of(2012, 12, 12))
                .build();

        Film addedFilm = filmController.addFilm(testFilm);

        assertEquals(testFilm.toString(), addedFilm.toString());
    }

    @Test
    public void shouldReturnFilmAfterMethodUpdateFilm() {

        Film testFilm = Film.builder()
                .name("Film")
                .description("DescFilm")
                .rate(1)
                .duration(120)
                .releaseDate(LocalDate.of(2012, 12, 12))
                .build();

        filmController.addFilm(testFilm);

        Film testUpdateFilm = Film.builder()
                .id(1L)
                .name("updateFilm")
                .description("updateDescFilm")
                .rate(1)
                .duration(80)
                .releaseDate(LocalDate.of(2012, 12, 10))
                .build();

        Film addedFilm = filmController.updateFilm(testUpdateFilm);

        assertEquals(testUpdateFilm.toString(), addedFilm.toString());
    }

    @Test
    public void shouldReturnAllFilmsAfterMethodFindAllFilms() {
        Film testFilm = Film.builder()
                .name("newFilm")
                .description("descFilm")
                .rate(1)
                .duration(120)
                .releaseDate(LocalDate.of(2012, 12, 12))
                .build();

        Film addedFilm = filmController.addFilm(testFilm);

        assertEquals(testFilm.toString(), addedFilm.toString());
    }
}

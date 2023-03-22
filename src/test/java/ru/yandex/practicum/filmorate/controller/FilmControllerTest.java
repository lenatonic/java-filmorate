package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.io.IOException;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class FilmControllerTest {
    private FilmController filmController = new FilmController();

    @Test
    public void shouldReturnFilmAfterMethodAddFilm() throws ValidationException {
        Film testFilm = Film.builder()
                .name("newFilm")
                .description("descFilm")
                .rate(1)
                .duration(120)
                .releaseDate(LocalDate.of(2012, 12, 12))
                .build();

        filmController.addFilm(testFilm);

        assertEquals(testFilm.toString(), filmController.getFilms().get(1).toString());
    }

    @Test
    public void shouldThrowExceptionAfterMethodAddFilmIfNoName() throws ValidationException {
        Film testFilm = Film.builder()
                .name(null)
                .description("descFilm")
                .rate(1)
                .duration(120)
                .releaseDate(LocalDate.of(2012, 12, 12))
                .build();

        Executable executable = () -> filmController.addFilm(testFilm);
        final ValidationException exception = assertThrows(
                ValidationException.class, executable);

        assertEquals("Не указано название фильма.", exception.getMessage());
    }

    @Test
    public void shouldThrowExceptionAfterMethodAddFilmIfNameIsBlank() throws ValidationException {
        Film testFilm = Film.builder()
                .name("")
                .description("descFilm")
                .rate(1)
                .duration(120)
                .releaseDate(LocalDate.of(2012, 12, 12))
                .build();

        Executable executable = () -> filmController.addFilm(testFilm);
        final ValidationException exception = assertThrows(
                ValidationException.class, executable);

        assertEquals("Не указано название фильма.", exception.getMessage());
    }

    @Test
    public void shouldThrowExceptionAfterMethodAddFilmIfDescriptionMore200() throws ValidationException {
        String desc = ("a");
        for (int i = 0; i < 201; i++) {
            desc = desc + "a";
        }
        Film testFilm = Film.builder()
                .name("newFilm")
                .description(desc)
                .rate(1)
                .duration(120)
                .releaseDate(LocalDate.of(2012, 12, 12))
                .build();

        Executable executable = () -> filmController.addFilm(testFilm);
        final ValidationException exception = assertThrows(
                ValidationException.class, executable);

        assertEquals("Описание превышает 200 символов.", exception.getMessage());
    }

    @Test
    public void shouldThrowExceptionAfterMethodAddFilmDurationIsMinus() throws ValidationException {
        Film testFilm = Film.builder()
                .name("newFilm")
                .description("descFilm")
                .rate(1)
                .duration(-120)
                .releaseDate(LocalDate.of(2012, 12, 12))
                .build();

        Executable executable = () -> filmController.addFilm(testFilm);
        final ValidationException exception = assertThrows(
                ValidationException.class, executable);

        assertEquals("Длительность фильма не может быть отрицательной", exception.getMessage());
    }

    @Test
    public void shouldThrowExceptionAfterMethodAddFilmIfReleaseDateWrong() throws ValidationException {
        Film testFilm = Film.builder()
                .name("newFilm")
                .description("descFilm")
                .rate(1)
                .duration(120)
                .releaseDate(LocalDate.of(1890, 12, 12))
                .build();

        Executable executable = () -> filmController.addFilm(testFilm);
        final ValidationException exception = assertThrows(
                ValidationException.class, executable);

        assertEquals("Дата релиза раньше 28-12-1895", exception.getMessage());
    }

    @Test
    public void shouldReturnFilmAfterMethodUpdateFilm() throws ValidationException, IOException {

        Film testFilm = Film.builder()
                .name("Film")
                .description("DescFilm")
                .rate(1)
                .duration(120)
                .releaseDate(LocalDate.of(2012, 12, 12))
                .build();

        filmController.addFilm(testFilm);

        Film testUpdateFilm = Film.builder()
                .id(1)
                .name("updateFilm")
                .description("updateDescFilm")
                .rate(1)
                .duration(80)
                .releaseDate(LocalDate.of(2012, 12, 10))
                .build();

        filmController.updateFilm(testUpdateFilm);

        assertEquals(testUpdateFilm.toString(), filmController.getFilms().get(1).toString());
    }

    @Test
    public void shouldThrowExceptionAfterMethodUpdateFilmIfNoName() throws ValidationException {
        Film testFilm = Film.builder()
                .name("Film")
                .description("DescFilm")
                .rate(1)
                .duration(120)
                .releaseDate(LocalDate.of(2012, 12, 12))
                .build();

        filmController.addFilm(testFilm);

        Film testUpdateFilm = Film.builder()
                .id(1)
                .name(null)
                .description("updateDescFilm")
                .rate(1)
                .duration(120)
                .releaseDate(LocalDate.of(2012, 12, 12))
                .build();

        Executable executable = () -> filmController.addFilm(testUpdateFilm);
        final ValidationException exception = assertThrows(
                ValidationException.class, executable);

        assertEquals("Не указано название фильма.", exception.getMessage());
    }

    @Test
    public void shouldThrowExceptionAfterMethodUpdateFilmIfNameIsBlank() throws ValidationException {

        Film testFilm = Film.builder()
                .name("Film")
                .description("DescFilm")
                .rate(1)
                .duration(120)
                .releaseDate(LocalDate.of(2012, 12, 12))
                .build();

        filmController.addFilm(testFilm);

        Film testUpdateFilm = Film.builder()
                .id(1)
                .name("")
                .description("updateDescFilm")
                .rate(1)
                .duration(120)
                .releaseDate(LocalDate.of(2012, 12, 12))
                .build();

        Executable executable = () -> filmController.addFilm(testUpdateFilm);
        final ValidationException exception = assertThrows(
                ValidationException.class, executable);

        assertEquals("Не указано название фильма.", exception.getMessage());
    }

    @Test
    public void shouldThrowExceptionAfterMethodUpdateFilmIfDescriptionMore200() throws ValidationException {
        Film testFilm = Film.builder()
                .name("Film")
                .description("DescFilm")
                .rate(1)
                .duration(120)
                .releaseDate(LocalDate.of(2012, 12, 12))
                .build();

        filmController.addFilm(testFilm);

        String desc = ("a");
        for (int i = 0; i < 201; i++) {
            desc = desc + "a";
        }

        Film testUpdateFilm = Film.builder()
                .id(1)
                .name("updateFilm")
                .description(desc)
                .rate(1)
                .duration(120)
                .releaseDate(LocalDate.of(2012, 12, 12))
                .build();

        Executable executable = () -> filmController.addFilm(testUpdateFilm);
        final ValidationException exception = assertThrows(
                ValidationException.class, executable);

        assertEquals("Описание превышает 200 символов.", exception.getMessage());
    }

    @Test
    public void shouldThrowExceptionAfterMethodUpdateFilmDurationIsMinus() throws ValidationException {
        Film testFilm = Film.builder()
                .name("Film")
                .description("DescFilm")
                .rate(1)
                .duration(120)
                .releaseDate(LocalDate.of(2012, 12, 12))
                .build();

        filmController.addFilm(testFilm);

        Film testUpdateFilm = Film.builder()
                .name("updateFilm")
                .description("updateDescFilm")
                .rate(1)
                .duration(-120)
                .releaseDate(LocalDate.of(2012, 12, 10))
                .build();

        Executable executable = () -> filmController.addFilm(testUpdateFilm);
        final ValidationException exception = assertThrows(
                ValidationException.class, executable);

        assertEquals("Длительность фильма не может быть отрицательной", exception.getMessage());
    }

    @Test
    public void shouldThrowExceptionAfterMethodUpdateFilmIfReleaseDateWrong() throws ValidationException {
        Film testFilm = Film.builder()
                .name("newFilm")
                .description("descFilm")
                .rate(1)
                .duration(120)
                .releaseDate(LocalDate.of(2012, 12, 12))
                .build();

        filmController.addFilm(testFilm);

        Film testUpdateFilm = Film.builder()
                .name("updateFilm")
                .description("updateDescFilm")
                .rate(1)
                .duration(120)
                .releaseDate(LocalDate.of(1890, 12, 10))
                .build();

        Executable executable = () -> filmController.addFilm(testUpdateFilm);
        final ValidationException exception = assertThrows(
                ValidationException.class, executable);

        assertEquals("Дата релиза раньше 28-12-1895", exception.getMessage());
    }

    @Test
    public void shouldReturnAllFilmsAfterMethodFindAllFilms() throws ValidationException {
        Film testFilm = Film.builder()
                .name("newFilm")
                .description("descFilm")
                .rate(1)
                .duration(120)
                .releaseDate(LocalDate.of(2012, 12, 12))
                .build();

        filmController.addFilm(testFilm);

        assertEquals(testFilm.toString(), filmController.findAllFilms().toString()
                .replace("[", "").replace("]", ""));
    }
}


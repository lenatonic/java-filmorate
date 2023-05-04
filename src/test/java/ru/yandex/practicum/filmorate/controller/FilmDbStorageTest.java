package ru.yandex.practicum.filmorate.controller;


import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.time.LocalDate;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FilmDbStorageTest {
    FilmStorage filmDbStorage;
    Film filmFirst;

    @BeforeEach
    public void getFilmFirst() {

//        film1 = new Film(1, "name", "", "", "",
//                LocalDate.of(1982, 06, 25), "", "", "");
//        film1.setName("Бегущий по лезвию бритвы");
//        film1.setDescription("Классика киберпанка по мотивам романа Филипа Дика.");
//        film1.setMpa(new Mpa(4));
//        film1.setDuration(117);
//        film1.setReleaseDate(LocalDate.of(1982, 06, 25));
//    }
        filmFirst = Film.builder()
                .name("Бегущий по лезвию бритвы")
                .description("Классика киберпанка по мотивам романа Филипа Дика.")
                .duration(117)
                .releaseDate(LocalDate.of(1982, 06, 25))
                .mpa(new Mpa(4))
                .build();
    }
//    @BeforeEach
//    void before() {

//    }

//    @AfterEach
//    void afterEach() {
//        for (Film film : filmStorage.findAllFilms()) {
//            filmStorage.(film);
//        }
//    }

    @Test
    void addFilmWithoutGenre() {
        Film testFilm = filmDbStorage.addFilm(filmFirst);
        //assertThat(testFilm.getId()).isNotNull();
        assertThat(testFilm.getName()).isEqualTo(filmFirst.getName());
//        AssertionsForClassTypes.assertThat(testFilm.getDescription()).isEqualTo(filmFirst.getDescription());
//        AssertionsForClassTypes.assertThat(testFilm.getReleaseDate()).isEqualTo(filmFirst.getReleaseDate());
//        AssertionsForClassTypes.assertThat(testFilm.getDuration()).isEqualTo(filmFirst.getDuration());
//        AssertionsForClassTypes.assertThat(testFilm.getMpa()).isEqualTo(filmFirst.getMpa());
//        AssertionsForClassTypes.assertThat(testFilm.getMpa().getName()).isEqualTo("R");

    }

}
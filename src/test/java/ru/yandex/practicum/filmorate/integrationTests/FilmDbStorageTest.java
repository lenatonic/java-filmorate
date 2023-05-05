package ru.yandex.practicum.filmorate.integrationTests;


import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.film.FilmDbStorage;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FilmDbStorageTest {
    final FilmDbStorage filmStorage;
    Film filmFirst;
    Film filmTwo;
    Film filmThree;
    Film filmUpdate;

    @BeforeEach
    public void start() {
        filmFirst = Film.builder()
                .name("Бегущий по лезвию бритвы")
                .description("Классика киберпанка по мотивам романа Филипа Дика.")
                .duration(117)
                .releaseDate(LocalDate.of(1982, 06, 25))
                .mpa(new Mpa(4))
                .build();

        Genre genre = Genre.builder()
                .id(2)
                .build();

        filmTwo = Film.builder()
                .name("Автостопом по галактике")
                .description("Уморительная комедия по роману Дугласа Адамса.")
                .duration(109)
                .releaseDate(LocalDate.of(2005, 04, 20))
                .mpa(new Mpa(2))
                .genres(new HashSet<>())
                .build();
        filmTwo.getGenres().add(genre);

        Genre genreForFilmTree1 = Genre.builder()
                .id(3)
                .build();
        Genre genreForFilmTree2 = Genre.builder()
                .id(6)
                .build();

        filmThree = Film.builder()
                .name("Призрак в доспехах")
                .description("Увлекательное аниме в стиле киберпанк")
                .duration(82)
                .releaseDate(LocalDate.of(1995, 11, 18))
                .mpa(new Mpa(3))
                .genres(new HashSet<>())
                .build();
        filmThree.getGenres().add(genreForFilmTree1);
        filmThree.getGenres().add(genreForFilmTree2);

        filmUpdate = Film.builder()
                .id(2L)
                .name("Призрак в доспехах. Режиссерская версия")
                .description("Увлекательное аниме в стиле киберпанк")
                .duration(140)
                .releaseDate(LocalDate.of(1995, 11, 18))
                .mpa(new Mpa(3))
                .genres(new HashSet<>())
                .build();
        filmThree.getGenres().add(genreForFilmTree1);
        filmThree.getGenres().add(genreForFilmTree2);
    }

    @Test
    void addFilmWithoutGenre() {
        Film testFilm = filmStorage.addFilm(filmFirst);
        assertThat(testFilm.getId()).isEqualTo(3);
        assertThat(testFilm.getName()).isEqualTo(filmFirst.getName());
        assertThat(testFilm.getDescription()).isEqualTo(filmFirst.getDescription());
        assertThat(testFilm.getReleaseDate()).isEqualTo(filmFirst.getReleaseDate());
        assertThat(testFilm.getDuration()).isEqualTo(filmFirst.getDuration());
        assertThat(testFilm.getMpa()).isEqualTo(filmFirst.getMpa());
        assertThat(testFilm.getMpa().getName()).isEqualTo("R");
    }

    @Test
    void addFilmWithOneGenre() {
        Film testFilm = filmStorage.addFilm(filmTwo);
        String genre = String.valueOf(testFilm.getGenres().stream()
                .map(a -> (a.getName())));
        assertThat(testFilm.getId()).isEqualTo(1);
        assertThat(testFilm.getName()).isEqualTo(filmTwo.getName());
        assertThat(testFilm.getDescription()).isEqualTo(filmTwo.getDescription());
        assertThat(testFilm.getReleaseDate()).isEqualTo(filmTwo.getReleaseDate());
        assertThat(testFilm.getDuration()).isEqualTo(filmTwo.getDuration());
        assertThat(testFilm.getMpa()).isEqualTo(filmTwo.getMpa());
        assertThat(testFilm.getMpa().getName()).isEqualTo("PG");
        assertThat(genre.contains("Комедия"));
    }

    @Test
    void addFilmWithTwoGenres() {
        Film testFilm = filmStorage.addFilm(filmThree);
        assertThat(testFilm.getId()).isEqualTo(2);
        assertThat(testFilm.getName()).isEqualTo(filmThree.getName());
        assertThat(testFilm.getDescription()).isEqualTo(filmThree.getDescription());
        assertThat(testFilm.getReleaseDate()).isEqualTo(filmThree.getReleaseDate());
        assertThat(testFilm.getDuration()).isEqualTo(filmThree.getDuration());
        assertThat(testFilm.getMpa()).isEqualTo(filmThree.getMpa());
        assertThat(testFilm.getMpa().getName()).isEqualTo("PG-13");
        assertThat(testFilm.getGenres().size()).isEqualTo(2);
    }

    @Test
    void updateFilm() {
        Film testFilm = filmStorage.updateFilm(filmUpdate);
        assertThat(testFilm.getId()).isEqualTo(2);
        assertThat(testFilm.getName()).isEqualTo(filmUpdate.getName());
        assertThat(testFilm.getDescription()).isEqualTo(filmUpdate.getDescription());
        assertThat(testFilm.getReleaseDate()).isEqualTo(filmUpdate.getReleaseDate());
        assertThat(testFilm.getDuration()).isEqualTo(filmUpdate.getDuration());
        assertThat(testFilm.getMpa()).isEqualTo(filmUpdate.getMpa());
        assertThat(testFilm.getMpa().getName()).isEqualTo("PG-13");
    }

    @Test
    void findAllFilms() {
        List<Film> allFilms = filmStorage.findAllFilms();
        assertThat(allFilms.size()).isEqualTo(3);
    }

    @Test
    void findFilm() {
        Film testFilm = filmStorage.findFilm(1L);
        filmTwo.getMpa().setName("PG");
        assertThat(testFilm.getName()).isEqualTo(filmTwo.getName());
        assertThat(testFilm.getDescription()).isEqualTo(filmTwo.getDescription());
        assertThat(testFilm.getReleaseDate()).isEqualTo(filmTwo.getReleaseDate());
        assertThat(testFilm.getDuration()).isEqualTo(filmTwo.getDuration());
        assertThat(testFilm.getMpa().getName()).isEqualTo(filmTwo.getMpa().getName());
    }
}

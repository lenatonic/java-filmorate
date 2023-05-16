package ru.yandex.practicum.filmorate.integrationTests;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.like.LikeDbStorage;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class LikeDbStorageTest {
    final LikeDbStorage likeDbStorage;
    final UserDbStorage userDbStorage;
    final FilmDbStorage filmDbStorage;

    User user1;
    User user2;
    Film filmFirst;
    Film filmTwo;

    @BeforeEach
    void start() {
        user1 = User.builder()
                .name("user1")
                .email("user1@test.com")
                .login("logUser1")
                .birthday(LocalDate.of(2001, 1, 1))
                .build();

        user2 = User.builder()
                .name("user2")
                .email("user2@test.com")
                .login("logUser2")
                .birthday(LocalDate.of(2002, 2, 2))
                .build();

        filmFirst = Film.builder()
                .name("Бегущий по лезвию бритвы")
                .description("Классика киберпанка по мотивам романа Филипа Дика.")
                .duration(117)
                .releaseDate(LocalDate.of(1982, 6, 25))
                .mpa(new Mpa(4))
                .build();

        Genre genre = Genre.builder()
                .id(1)
                .build();

        filmTwo = Film.builder()
                .name("Автостопом по галактике")
                .description("Уморительная комедия по роману Дугласа Адамса.")
                .duration(109)
                .releaseDate(LocalDate.of(2005, 4, 20))
                .mpa(new Mpa(2))
                .genres(new HashSet<>())
                .build();
        filmTwo.getGenres().add(genre);

        userDbStorage.addUser(user1);
        userDbStorage.addUser(user2);

        filmDbStorage.addFilm(filmFirst);
        filmDbStorage.addFilm(filmTwo);
    }

    @AfterEach
    void end() {
        userDbStorage.deleteAllUser();
    }

    @Test
    void testAddLike() {
        likeDbStorage.addLike(1L, 1L);
        List<Film> likeFilm1 = filmDbStorage.findTop(10);
        likeDbStorage.addLike(1L, 2L);
        likeDbStorage.addLike(2L, 2L);
        List<Film> likeFilm12 = filmDbStorage.findTop(5);
        likeDbStorage.deleteLike(2L, 2L);
        List<Film> likeFilmAfterDelete = filmDbStorage.findTop(5);
        assertThat(likeFilm1.get(0)).isEqualTo(filmFirst);
        assertThat(likeFilm12.get(0)).isEqualTo(filmFirst);
        assertThat(likeFilm12.get(1)).isEqualTo(filmTwo);
        assertThat(likeFilmAfterDelete.get(0)).isEqualTo(filmFirst);
    }
}
package ru.yandex.practicum.filmorate.integrationTests;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.like.LikeDbStorage;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;

import javax.validation.constraints.Max;
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
                .birthday(LocalDate.of(2001, 01, 01))
                .build();

        user2 = User.builder()
                .name("user2")
                .email("user2@test.com")
                .login("logUser2")
                .birthday(LocalDate.of(2002, 02, 02))
                .build();

        filmFirst = Film.builder()
                .name("Бегущий по лезвию бритвы")
                .description("Классика киберпанка по мотивам романа Филипа Дика.")
                .duration(117)
                .releaseDate(LocalDate.of(1982, 06, 25))
                .mpa(new Mpa(4))
                .build();

        filmTwo = Film.builder()
                .name("Автостопом по галактике")
                .description("Уморительная комедия по роману Дугласа Адамса.")
                .duration(109)
                .releaseDate(LocalDate.of(2005, 04, 20))
                .mpa(new Mpa(2))
                .genres(new HashSet<>())
                .build();

        userDbStorage.addUser(user1);
        userDbStorage.addUser(user2);

        filmDbStorage.addFilm(filmFirst);
        filmDbStorage.addFilm(filmTwo);

    }
    @Test
    void testAddLike() {
        likeDbStorage.addLike(1L, 1L);
        List<Long> likeFilm1 = likeDbStorage.findTop(10);

        likeDbStorage.addLike(1L, 2L);
        likeDbStorage.addLike(2L, 2L);
        List<Long> likeFilm12 = likeDbStorage.findTop(5);

        likeDbStorage.deleteLike(2L, 2L);
        List<Long> likeFilmAfterDelete = likeDbStorage.findTop(5);

        assertThat(likeFilm1.get(0)).isEqualTo(1);
        assertThat(likeFilm12.get(0)).isEqualTo(1);
        assertThat(likeFilm12.get(1)).isEqualTo(2);
        assertThat(likeFilmAfterDelete.get(0)).isEqualTo(1);

    }
}

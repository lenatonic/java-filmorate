package ru.yandex.practicum.filmorate.integrationTests;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.genre.GenreDbStorage;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class GenreDbStorageTest {
    final GenreDbStorage genreDbStorage;

    @Test
    void testFindAllGenres() {
        List<Genre> allGenre = genreDbStorage.findAllGenres();
        assertThat(allGenre.get(0).getName()).isEqualTo("Комедия");
        assertThat(allGenre.get(1).getName()).isEqualTo("Драма");
        assertThat(allGenre.get(2).getName()).isEqualTo("Мультфильм");
        assertThat(allGenre.get(3).getName()).isEqualTo("Триллер");
        assertThat(allGenre.get(4).getName()).isEqualTo("Документальный");
        assertThat(allGenre.get(5).getName()).isEqualTo("Боевик");
    }

    @Test
    void testFindGenre() {
        Genre result = genreDbStorage.findGenre(1);
        assertThat(result.getName()).isEqualTo("Комедия");
    }
}

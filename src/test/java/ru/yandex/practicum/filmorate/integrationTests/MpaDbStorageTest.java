package ru.yandex.practicum.filmorate.integrationTests;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.mpa.MpaDbStorage;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class MpaDbStorageTest {
    final MpaDbStorage mpaDbStorage;

    @Test
    void testFindAllMpa() {
        List<Mpa> allMpa = mpaDbStorage.findAllMpa();
        assertThat(allMpa.get(0).getName()).isEqualTo("G");
        assertThat(allMpa.get(1).getName()).isEqualTo("PG");
        assertThat(allMpa.get(2).getName()).isEqualTo("PG-13");
        assertThat(allMpa.get(3).getName()).isEqualTo("R");
        assertThat(allMpa.get(4).getName()).isEqualTo("NC-17");
    }

    @Test
    void testFindMpa() {
        Mpa mpa = mpaDbStorage.findMpa(1);
        assertThat(mpa.getName()).isEqualTo("G");
    }
}
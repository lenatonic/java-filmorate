package ru.yandex.practicum.filmorate.storage;

import lombok.Data;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.HashMap;

@Component
@Data
public class InMemoryFilmStorage implements FilmStorage {
    private final HashMap<Long, Film> films = new HashMap<>();
    private Long id = 0L;

    @Override
    public Film addFilm(Film film) {
        film.setId(++id);
        films.put(id, film);
        return films.get(id);
    }

    @Override
    public Film updateFilm(Film film) {
        if (!films.containsKey(film.getId())) {
            throw new NotFoundException("Неверно указан id");
        }
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public ArrayList<Film> findAllFilms() {
        return new ArrayList<>(films.values());
    }

    public void validationIdFilm(Long id) {
        if (!films.containsKey(id)) {
            throw new NotFoundException("Нет пользователя с таким id");
        }
    }
}
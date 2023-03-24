package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;

@Slf4j
@RestController
public class FilmController {
    private final HashMap<Integer, Film> films = new HashMap<>();
    private int id = 0;

    @PostMapping(value = "/films")
    public Film addFilm(@Valid @RequestBody Film film) {
        film.setId(++id);
        films.put(id, film);
        log.debug("Добавлен новый фильм: ");
        return film;
    }

    @PutMapping(value = "/films")
    public Film updateFilm(@Valid @RequestBody Film film) {
        if (!films.containsKey(film.getId())) {
            throw new NotFoundException("Неверно указан id");
        }
        films.put(film.getId(), film);
        log.debug("Внесены изменения фильма: ");
        return film;
    }

    @GetMapping(value = "/films")
    public ArrayList<Film> findAllFilms() {
        ArrayList<Film> filmList = new ArrayList<>();
        log.debug("Выводим список фильмов: ");
        return new ArrayList<>(films.values());
    }

    public HashMap<Integer, Film> getFilms() {
        return films;
    }
}

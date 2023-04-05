package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;

import javax.validation.Valid;
import java.util.ArrayList;


@Slf4j
@RestController
public class FilmController {

    private final InMemoryFilmStorage inMemoryFilmStorage;
    private final FilmService filmService;

    @Autowired
    public FilmController(InMemoryFilmStorage inMemoryFilmStorage, FilmService filmService) {
        this.inMemoryFilmStorage = inMemoryFilmStorage;
        this.filmService = filmService;
    }

    @PostMapping(value = "/films")
    public Film addFilm(@Valid @RequestBody Film film) {
        log.debug("Добавлен новый фильм: " + film.getName());
        Film addedFilm = inMemoryFilmStorage.addFilm(film);
        filmService.addLike(film.getId(), null);

        return film;
    }

    @PutMapping(value = "/films")
    public Film updateFilm(@Valid @RequestBody Film film) {

        log.debug("Внесены изменения фильма: " + film.getName());
        return inMemoryFilmStorage.updateFilm(film);
    }

    @PutMapping(value = "/films/{id}/like/{userId}")
    public Long addLike(@PathVariable("id") Long id,
                        @PathVariable("userId") Long userId) {
        log.debug("Ставим like фильму ");
        return filmService.addLike(id, userId);
    }

    @GetMapping(value = "/films")
    public ArrayList<Film> findAllFilms() {

        log.debug("Выводим список фильмов: ");
        return inMemoryFilmStorage.findAllFilms();
    }

    @GetMapping(value = "/films/{id}")
    public Film findFilm(@PathVariable("id") Long id) {
        inMemoryFilmStorage.validationIdFilm(id);
        log.debug("Находим фильм по id: " + id);
        return inMemoryFilmStorage.getFilms().get(id);
    }

    @GetMapping(value = "/films/popular")
    public ArrayList<Film> findTop(@RequestParam(defaultValue = "10") Integer count) {
        ArrayList<Film> top = new ArrayList<>();
        ArrayList<Long> request = filmService.findTop(count);
        for (Long id : request) {
            top.add(inMemoryFilmStorage.getFilms().get(id));
        }
        log.debug("Находим список популярных фильмов: ");
        return top;
    }

    @DeleteMapping(value = "/films/{id}/like/{userId}")
    public Long deleteLike(@PathVariable("id") Long id,
                           @PathVariable("userId") Long userId) {
        log.debug("Удаляем лайк у фильма с id: " + id);
        return filmService.deleteLike(id, userId);
    }
}


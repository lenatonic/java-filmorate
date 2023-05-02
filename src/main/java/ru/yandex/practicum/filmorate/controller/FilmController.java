package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
//@RequiredArgsConstructor
public class FilmController {
    private final FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @PostMapping(value = "/films")
    public Film addFilm(@Valid @RequestBody Film film) {
        Film request = filmService.addFilm(film);
        log.debug("Добавлен новый фильм: " + film.getName());
        return request;
    }

    @PutMapping(value = "/films")
    public Film updateFilm(@Valid @RequestBody Film film) {
        Film request = filmService.updateFilm(film);
        log.debug("Внесены изменения фильма: " + film.getName());
        return request;
    }

    @GetMapping(value = "/films")
    public List<Film> findAllFilms() {
        List<Film> request = filmService.findAllFilms();
        log.debug("Выводим список фильмов: ");
        return request;
    }

    @GetMapping(value = "/films/{id}")
    public Film findFilm(@PathVariable("id") Long id) {
        Film request = filmService.findFilm(id);
        log.debug("Находим фильм по id: " + id);
        return request;
    }

//    @PutMapping(value = "/films/{id}/like/{userId}")
//    public Film addLike(@PathVariable("id") Long id,
//                        @PathVariable("userId") Long userId) {
//        Film request = filmService.addLike(id, userId);
//        log.debug("Ставим like фильму ");
//        return request;
//    }


//    @GetMapping(value = "/films/popular")
//    public List<Film> findTop(@RequestParam(defaultValue = "10") Integer count) {
//        List<Film> request = filmService.findTop(count);
//        log.debug("Находим список популярных фильмов: ");
//        return request;
//    }
//
//    @DeleteMapping(value = "/films/{id}/like/{userId}")
//    public Long deleteLike(@PathVariable("id") Long id,
//                           @PathVariable("userId") Long userId) {
//        Long request = filmService.deleteLike(id, userId);
//        log.debug("Удаляем лайк у фильма с id: " + id);
//        return request;
//    }
}
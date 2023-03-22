package ru.yandex.practicum.filmorate.controller;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;

@Data
@Slf4j
@RestController
public class FilmController {
    private final HashMap<Integer, Film> films = new HashMap<>();
    int id = 1;

    @PostMapping(value = "/films")
    public Film addFilm(@RequestBody Film film) throws ValidationException {
        validationFilm(film);
        for (Film film1 : films.values()) {
            if (film.equals(film1)) {
                return film1;
            }
        }
        film.setId(id);
        films.put(id, film);
        log.debug("Добавлен новый фильм: ");
        id++;
        return film;
    }

    @PutMapping(value = "/films")
    public Film updateFilm(@RequestBody Film film) throws ValidationException, IOException {
        if (!films.containsKey(film.getId())) {
            throw new ValidationException("Неверно указан id");
        }
        validationFilm(film);
        films.put(film.getId(), film);
        log.debug("Внесены изменения фильма: ");
        return film;
    }

    @GetMapping(value = "/films")
    public ArrayList<Film> findAllFilms() {
        ArrayList<Film> filmList = new ArrayList<>();
        films.keySet().stream()
                .forEach((id) -> filmList.add(films.get(id)));
        log.debug("Выводим список фильмов: ");
        return filmList;
    }

    public void validationFilm(Film film) throws ValidationException {
        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        if (film.getName() == null || film.getName().isBlank()) {
            log.error("Отсутствует название фильма.");
            throw new ValidationException("Не указано название фильма.");
        }
        if (film.getDescription().length() > 200) {
            log.error("В описании более 200 символов.");
            throw new ValidationException("Описание превышает 200 символов.");
        }
        if (film.getReleaseDate().isBefore(LocalDate.parse("28-12-1895", formatter))) {
            log.error("Дата релиза задана неверно.");
            throw new ValidationException("Дата релиза раньше 28-12-1895");
        }

        if (film.getDuration() < 0) {
            log.error("Длительность фильма задана не верно.");
            throw new ValidationException("Длительность фильма не может быть отрицательной");
        }
    }
}

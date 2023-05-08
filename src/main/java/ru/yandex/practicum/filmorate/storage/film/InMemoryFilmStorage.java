//package ru.yandex.practicum.filmorate.storage.film;
//
//import org.springframework.stereotype.Component;
//import ru.yandex.practicum.filmorate.exception.NotFoundException;
//import ru.yandex.practicum.filmorate.model.Film;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.HashSet;
//import java.util.List;
//
//@Component
//public class InMemoryFilmStorage implements FilmStorage {
//    private HashMap<Long, Film> films = new HashMap<>();
//    private Long id = 0L;
//
//    @Override
//    public Film addFilm(Film film) {
//        Film addedFilm = film;
//        addedFilm.setLikes(new HashSet<>());
//        film.setId(++id);
//        films.put(film.getId(), addedFilm);
//        return film;
//    }
//
//    @Override
//    public Film updateFilm(Film film) {
//        if (!films.containsKey(film.getId())) {
//            throw new NotFoundException("Неверно указан id");
//        }
//        Film updateFilm = film;
//        updateFilm.setLikes(films.get(film.getId()).getLikes());
//        films.put(film.getId(), updateFilm);
//        return film;
//    }
//
//    @Override
//    public List<Film> findAllFilms() {
//        return new ArrayList<>(films.values());
//    }
//
//    public Film findFilm(Long id) {
//        if (!films.containsKey(id)) {
//            throw new NotFoundException("Нет фильма с таким id");
//        }
//        return films.get(id);
//    }
//}
package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.like.LikeDbStorage;

import java.util.ArrayList;
import java.util.List;

@Service
public class FilmService {
    private final FilmStorage filmDbStorage;
    private final UserService userService;
    private final LikeDbStorage likeDbStorage;

    @Autowired
    public FilmService(@Qualifier("filmDbStorage") FilmStorage filmDbStorage, LikeDbStorage likeDbStorage,
                       UserService userService) {
        this.filmDbStorage = filmDbStorage;
        this.userService = userService;
        this.likeDbStorage = likeDbStorage;
    }

    public Film addFilm(Film film) {
        return filmDbStorage.addFilm(film);
    }

    public Film updateFilm(Film film) {
        return filmDbStorage.updateFilm(film);
    }

    public List<Film> findAllFilms() {
        return filmDbStorage.findAllFilms();
    }

    public Film findFilm(Long id) {
        return filmDbStorage.findFilm(id);
    }

    public Film addLike(Long idFilm, Long idUser) {
        Film result = null;
        Film addedLikeFilm = filmDbStorage.findFilm(idFilm);
        userService.findUser(idUser);
        if (likeDbStorage.addLike(idFilm, idUser) > 0) {
            result = addedLikeFilm;
        }
        return result;
    }

    public Long deleteLike(Long idFilm, Long idUser) {
        filmDbStorage.findFilm(idFilm);
        userService.findUser(idUser);
        return likeDbStorage.deleteLike(idFilm, idUser);
    }

    public List<Film> findTop(int count) {
        List<Film> top = new ArrayList<>();
        List<Long> idFilms = filmDbStorage.findTop(count);
        for (Long id : idFilms) {
            top.add(findFilm(id));
        }
        return top;
    }
}
package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
//@RequiredArgsConstructor
public class FilmService {
    private final FilmStorage filmDbStorage;
    private final UserService userService;

    @Autowired
    public FilmService(@Qualifier("filmDbStorage") FilmStorage filmDbStorage,
                       UserService userService) {
        this.filmDbStorage = filmDbStorage;
        this.userService = userService;
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

    //    public Film addLike(Long idFilm, Long idUser) {
//        Film addedLikeFilm = inMemoryFilmStorage.findFilm(idFilm);
//        userService.findUser(idUser);
//        addedLikeFilm.getLikes().add(idUser);
//        return addedLikeFilm;
//    }

//    public Long deleteLike(Long idFilm, Long idUser) {
//        Film deleteLikeFilm = inMemoryFilmStorage.findFilm(idFilm);
//        if (deleteLikeFilm.getLikes().contains(idUser)) {
//            deleteLikeFilm.getLikes().remove(idUser);
//        } else {
//            throw new NotFoundException("Вы не ставили лайк этому фильму");
//        }
//        return idUser;
//    }
//
//    public List<Film> findTop(Integer count) {
//        List<Film> top = new ArrayList<>();
//        int control = 0;
//
//        List<Film> list = inMemoryFilmStorage.findAllFilms().stream()
//                .sorted((Comparator.comparingInt(x -> x.getLikes().size())))
//                .collect(Collectors.toList());
//        Collections.reverse(list);
//
//        if (count > inMemoryFilmStorage.findAllFilms().size()) {
//            count = inMemoryFilmStorage.findAllFilms().size();
//        }
//
//        for (Film film : list) {
//            if (control == count) {
//                break;
//            }
//            for (int i = 0; i < count; i++) {
//                top.add(film);
//                control++;
//            }
//        }
//        return top;
//    }
}
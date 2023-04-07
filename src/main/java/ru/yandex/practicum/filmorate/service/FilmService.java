package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;

import java.util.*;

@Service
@RequiredArgsConstructor
public class FilmService {
    private final InMemoryFilmStorage inMemoryFilmStorage;

    public Film addFilm(Film film) {
        return inMemoryFilmStorage.addFilm(film);
    }

    public Film updateFilm(Film film) {
        return inMemoryFilmStorage.updateFilm(film);
    }

    public Film addLike(Long idFilm, Long idUser) {
        Film addedLikeFilm = inMemoryFilmStorage.findFilm(idFilm);
        inMemoryFilmStorage.findFilm(idFilm);
        inMemoryFilmStorage.findFilm(idFilm).getLikes().add(idUser);
        return addedLikeFilm;
    }

    public List<Film> findAllFilms() {
        return inMemoryFilmStorage.findAllFilms();
    }

    public Film findFilm(Long id) {
        return inMemoryFilmStorage.findFilm(id);
    }

    public Long deleteLike(Long idFilm, Long idUser) {
        Film deleteLikeFilm = inMemoryFilmStorage.findFilm(idFilm);
        if (deleteLikeFilm.getLikes().contains(idUser)) {
            inMemoryFilmStorage.findFilm(idFilm).getLikes().remove(idUser);
        } else {
            throw new NotFoundException("Вы не ставили лайк этому фильму");
        }
        return idUser;
    }

    public List<Film> findTop(Integer count) {
        List<Film> top = new ArrayList<>();
        List list = new ArrayList(inMemoryFilmStorage.getFilms().entrySet());
        int control = 0;
        Collections.sort(list, Comparator.comparing(
                (Map.Entry<Long, Film> a) -> a.getValue().getLikes().size()).reversed());

        for (Object el : list) {
            Map.Entry<Long, Film> map = (Map.Entry<Long, Film>) el;
            if (control == count) {
                break;
            }
            top.add(map.getValue());
            control++;
        }
        return top;
    }
}
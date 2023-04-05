package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;

import java.util.*;

@Service
public class FilmService {
    private final HashMap<Long, Set<Long>> likes = new HashMap<>();

    public Long addLike(Long idFilm, Long idUser) {
        if (idUser == null) {
            likes.put(idFilm, new HashSet<>());
            return idFilm;
        }
        if (likes.containsKey(idFilm)) {
            likes.get(idFilm).add(idUser);
        } else {
            likes.put(idFilm, new HashSet<>());
            likes.get(idFilm).add(idUser);
        }
        return idFilm;
    }

    public Long deleteLike(Long idFilm, Long idUser) {
        if (likes.containsKey(idFilm) && likes.get(idFilm).contains(idUser)) {
            likes.get(idFilm).remove(idUser);
        } else {
            throw new NotFoundException("Вы не ставили лайк этому фильму");
        }
        return idUser;
    }

    public ArrayList<Long> findTop(Integer count) {
        ArrayList<Long> top = new ArrayList<>();
        List list = new ArrayList(likes.entrySet());
        int control = 0;

        Collections.sort(list, Comparator.comparingLong(
                (Map.Entry<Long, HashSet<Integer>> a) -> a.getValue().size()).reversed());
        if (count > list.size()) {
            count = list.size();
        }
        for (Object el : list) {
            Map.Entry<Long, HashSet<Integer>> map = (Map.Entry<Long, HashSet<Integer>>) el;
            if (control == count) {
                break;
            }
            top.add(map.getKey());
            control++;
        }
        return top;
    }
}

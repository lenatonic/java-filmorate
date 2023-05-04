package ru.yandex.practicum.filmorate.storage.like;

import java.util.List;

public interface LikeStorage {
    Long addLike(Long idFilm, Long idUser);

    Long deleteLike(Long idFilm, Long idUser);

    List<Long> findTop(int count);
}

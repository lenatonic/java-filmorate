package ru.yandex.practicum.filmorate.storage.like;

public interface LikeStorage {
    Long addLike(Long idFilm, Long idUser);

    Long deleteLike(Long idFilm, Long idUser);
}
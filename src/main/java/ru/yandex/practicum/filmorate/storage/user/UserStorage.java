package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {
    User addUser(User user);

    User updateUser(User user);

    List<User> findAllUser();

    User findUser(Long id);

    List<User> findCommonFriends(Long user1, Long user2);
}
package ru.yandex.practicum.filmorate.storage.friendship;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface FriendshipStorage {
    Long addFriend(Long userId, Long friendId);

    List<User> findAllFriends(Long userId);

    List<User> findCommonFriends(Long user1, Long user2);

    Long deleteFriend(Long userId, Long friendId);
}
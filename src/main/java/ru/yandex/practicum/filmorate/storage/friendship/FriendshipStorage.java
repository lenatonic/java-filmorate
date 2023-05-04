package ru.yandex.practicum.filmorate.storage.friendship;

import java.util.List;

public interface FriendshipStorage {
    Long addFriend(Long userId, Long friendId);

    List<Long> findAllFriends(Long userId);

    List<Long> findCommonFriends(Long user1, Long user2);

    Long deleteFriend(Long userId, Long friendId);
}

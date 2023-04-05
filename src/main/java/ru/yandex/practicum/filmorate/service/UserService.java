package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

@Service
public class UserService {
    HashMap<Long, Set<Long>> friends = new HashMap<>();

    public Long addFriend(Long idUser, Long idFriend) {
        if (friends.containsKey(idUser)) {
            if (friends.get(idUser).contains(idFriend)) {
                throw new NotFoundException("Пользователь уже в друзьях");
            }
            friends.get(idUser).add(idFriend);
        } else {
            friends.put(idUser, new HashSet<>());
            friends.get(idUser).add(idFriend);
        }
        if (friends.containsKey(idFriend)) {
            friends.get(idFriend).add(idUser);
        } else {
            friends.put(idFriend, new HashSet<>());
            friends.get(idFriend).add(idUser);
        }
        return idUser;
    }

    public Long deleteFriend(Long idUser, Long idFriend) {
        if (!friends.get(idUser).contains(idFriend)) {
            throw new NotFoundException("Ошибка при удалении. Нет друга с таким id");
        }
        friends.get(idUser).remove(idFriend);
        return idUser;
    }

    public Set<Long> findAllFriends(Long idUser) {
        if (friends.containsKey(idUser)) {
            return friends.get(idUser);
        } else {
            throw new NotFoundException("У этого пользователя пока нет друзей");
        }
    }

    public ArrayList<Long> findCommonFriends(Long idUser, Long otherId) {
        ArrayList<Long> commonFriends = new ArrayList<>();
        if (!friends.containsKey(idUser) || !friends.containsKey(otherId)) {
            return commonFriends;
        }
        ArrayList<Long> friendsUser = new ArrayList<>();
        ArrayList<Long> friendsOtherUser = new ArrayList<>();
        friendsUser.addAll(friends.get(idUser));
        friendsOtherUser.addAll(friends.get(otherId));
        for (long id : friendsUser) {
            if (friendsOtherUser.contains(id)) {
                commonFriends.add(id);
            }
        }
        return commonFriends;
    }
}

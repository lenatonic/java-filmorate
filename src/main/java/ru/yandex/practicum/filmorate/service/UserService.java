package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@SpringBootApplication
public class UserService {

    private final InMemoryUserStorage inMemoryUserStorage;

    public User addUser(User user) {
        return inMemoryUserStorage.addUser(user);
    }

    public User updateUser(User user) {
        return inMemoryUserStorage.updateUser(user);
    }

    public List<User> findAllUser() {
        return inMemoryUserStorage.findAllUser();
    }

    public User addFriend(Long idUser, Long idFriend) {
        User user = inMemoryUserStorage.findUser(idUser);
        User friendUser = inMemoryUserStorage.findUser(idFriend);

        if (user.getFriends()
                .contains(idFriend)) {
            throw new NotFoundException("Пользователь уже в друзьях");
        }
        user.getFriends().add(idFriend);
        friendUser.getFriends().add(idUser);
        return user;
    }

    public User findUser(Long id) {
        return inMemoryUserStorage.findUser(id);
    }

    public User deleteFriend(Long idUser, Long idFriend) {
        User user = inMemoryUserStorage.findUser(idUser);
        User friend = inMemoryUserStorage.findUser(idFriend);
        if (!user.getFriends().contains(idFriend)) {
            throw new NotFoundException("Ошибка при удалении. Нет друга с таким id");
        }
        friend.getFriends().remove(idUser);
        user.getFriends().remove(idFriend);
        return user;
    }

    public List<User> findAllFriends(Long idUser) {
        User user = inMemoryUserStorage.findUser(idUser);
        List<User> allFriends = new ArrayList<>();
        Set<Long> allFriendsId = new HashSet<>();
        allFriendsId = user.getFriends();
        for (Long friendId : allFriendsId) {
            allFriends.add(inMemoryUserStorage.findUser(friendId));
        }
        return allFriends;
    }

    public List<User> findCommonFriends(Long idUser, Long otherId) {
        User user = inMemoryUserStorage.findUser(idUser);
        User otherUser = inMemoryUserStorage.findUser(otherId);

        List<User> commonFriends = user.getFriends().stream()
                .filter(id -> otherUser.getFriends().contains(id))
                .map(inMemoryUserStorage::findUser)
                .collect(Collectors.toList());
        return commonFriends;
    }
}
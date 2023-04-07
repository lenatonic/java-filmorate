package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor

public class UserService {

    private final InMemoryUserStorage inMemoryUserStorage;

    public User addUser(User user) {
        return inMemoryUserStorage.addUser(user);
    }

    public User updateUser(User user) {
        return inMemoryUserStorage.updateUser(user);
    }

    public ArrayList<User> findAllUser() {
        return inMemoryUserStorage.findAllUser();
    }


    public User addFriend(Long idUser, Long idFriend) {
        User user = inMemoryUserStorage.findUser(idUser);
        User friendUser = inMemoryUserStorage.findUser(idFriend);

        if (inMemoryUserStorage.getUsers().get(idUser).getFriends()
                .contains(idFriend)) {
            throw new NotFoundException("Пользователь уже в друзьях");
        }
        inMemoryUserStorage.getUsers().get(idUser).getFriends().add(idFriend);
        inMemoryUserStorage.getUsers().get(idFriend).getFriends().add(idUser);
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
        user.getFriends().remove(idFriend);
        return user;
    }

    public List<User> findAllFriends(Long idUser) {
        User user = inMemoryUserStorage.findUser(idUser);
        List<User> allFriends = new ArrayList<>();
        Set<Long> allFriendsId = new HashSet<>();
        if (user.getFriends().isEmpty()) {
            throw new NotFoundException("У этого пользователя пока нет друзей");
        } else {
            allFriendsId = user.getFriends();
        }
        for (Long friendId : allFriendsId) {
            allFriends.add(inMemoryUserStorage.getUsers().get(friendId));
        }
        return allFriends;
    }

    public List<User> findCommonFriends(Long idUser, Long otherId) {
        User user = inMemoryUserStorage.findUser(idUser);
        User otherUser = inMemoryUserStorage.findUser(otherId);
        List<User> commonFriends = new ArrayList<>();

        if (user.getFriends().isEmpty() || otherUser.getFriends().isEmpty()) {
            return commonFriends;
        }

        List<User> friendsUser = new ArrayList<>();
        List<User> friendsOtherUser = new ArrayList<>();

        for (Long userFriend : user.getFriends()) {
            friendsUser.add(inMemoryUserStorage.findUser(userFriend));
        }

        for (Long otherFriend : otherUser.getFriends()) {
            friendsOtherUser.add(inMemoryUserStorage.findUser(otherFriend));
        }

        for (User friend : friendsUser) {
            if (friendsOtherUser.contains(friend)) {
                commonFriends.add(friend);
            }
        }
        return commonFriends;
    }
}

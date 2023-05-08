package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.friendship.FriendshipStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;

@Service
@Slf4j
public class UserService {
    private UserStorage userDbStorage;
    private FriendshipStorage friendshipDbStorage;

    @Autowired
    public UserService(@Qualifier("userDbStorage") UserStorage userDbStorage,
                       FriendshipStorage friendshipDbStorage) {
        this.userDbStorage = userDbStorage;
        this.friendshipDbStorage = friendshipDbStorage;
    }

    public User addUser(User user) {
        validationUserName(user);
        return userDbStorage.addUser(user);
    }

    public User updateUser(User user) {
        validationUserName(user);
        return userDbStorage.updateUser(user);
    }

    public List<User> findAllUser() {
        return userDbStorage.findAllUser();
    }

    public User findUser(Long id) {
        return userDbStorage.findUser(id);
    }

    public User addFriend(Long idUser, Long idFriend) {
        userDbStorage.findUser(idUser);
        userDbStorage.findUser(idFriend);
        friendshipDbStorage.addFriend(idUser, idFriend);
        User user = userDbStorage.findUser(idUser);
        return user;
    }

    public User deleteFriend(Long idUser, Long idFriend) {
        friendshipDbStorage.deleteFriend(idUser, idFriend);
        User user = userDbStorage.findUser(idUser);
        return user;
    }

    public List<User> findAllFriends(Long idUser) {
        List<User> allFriends = friendshipDbStorage.findAllFriends(idUser);
        System.out.println(allFriends);
        return allFriends;
    }

    public List<User> findCommonFriends(Long idUser, Long otherId) {
        List<User> commonFriends = friendshipDbStorage.findCommonFriends(idUser, otherId);
        return commonFriends;
    }

    private void validationUserName(User user) {
        if (user.getName() == null || user.getName().equals(" ") || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }
}
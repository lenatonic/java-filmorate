package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Set;


@Slf4j
@RestController
public class UserController {
    private final InMemoryUserStorage inMemoryUserStorage;
    private final UserService userService;

    @Autowired
    public UserController(InMemoryUserStorage inMemoryUserStorage, UserService userService) {
        this.inMemoryUserStorage = inMemoryUserStorage;
        this.userService = userService;
    }

    @PostMapping(value = "/users")
    public User addUser(@Valid @RequestBody User user) {
        log.debug("Создан новый пользователь: ", user);
        return inMemoryUserStorage.addUser(user);
    }

    @PutMapping(value = "/users")
    public User updateUser(@Valid @RequestBody User user) {
        log.debug("Обновлены данные для пользователя: ", user.getId());
        return inMemoryUserStorage.updateUser(user);
    }

    @PutMapping(value = "/users/{id}/friends/{friendId}")
    public User addFriend(@PathVariable("id") Long id,
                          @PathVariable("friendId") Long friendId) {
        inMemoryUserStorage.validationIdUser(id);
        inMemoryUserStorage.validationIdUser(friendId);
        Long request = userService.addFriend(id, friendId);
        log.debug("Добавляем user с id: " + friendId + " в друзья к user " + id);
        return inMemoryUserStorage.getUsers().get(request);
    }

    @GetMapping(value = "/users")
    public ArrayList<User> findAllUser() {
        log.debug("Находим список всех пользователей: ");
        return inMemoryUserStorage.findAllUser();
    }

    @GetMapping(value = "/users/{id}")
    public User findUser(@PathVariable("id") Long id) {
        inMemoryUserStorage.validationIdUser(id);
        log.debug("Находим пользователя по id: " + id);
        return inMemoryUserStorage.getUsers().get(id);
    }

    @GetMapping(value = "/users/{id}/friends")
    public ArrayList<User> findAllFriends(@PathVariable("id") Long id) {
        inMemoryUserStorage.validationIdUser(id);
        Set<Long> allFriendsId = userService.findAllFriends(id);
        ArrayList<User> allFriends = new ArrayList<>();
        for (Long friendId : allFriendsId) {
            allFriends.add(inMemoryUserStorage.getUsers().get(friendId));
        }
        log.debug("Список друзей пользователя с id: " + id);
        return allFriends;
    }

    @GetMapping(value = "/users/{id}/friends/common/{otherId}")
    public ArrayList<User> findCommonFriends(@PathVariable("id") Long id,
                                             @PathVariable("otherId") Long otherId) {
        inMemoryUserStorage.validationIdUser(id);
        inMemoryUserStorage.validationIdUser(otherId);
        ArrayList<Long> request = userService.findCommonFriends(id, otherId);
        ArrayList<User> commonFriends = new ArrayList<>();
        log.debug("Список общих друзей пользователей с id: " + id + " и " + otherId);
        if (request.isEmpty()) {
            return commonFriends;
        }
        request.stream()
                .forEach((i) -> commonFriends.add(inMemoryUserStorage.getUsers().get(i)));
        return commonFriends;
    }

    @DeleteMapping(value = "/users/{id}/friends/{friendId}")
    public User deleteFriend(@PathVariable("id") Long id,
                             @PathVariable("friendId") Long friendId) {
        Long request = userService.deleteFriend(id, friendId);
        log.debug("Удаляем user с id: " + friendId + " из друзей user " + id);
        return inMemoryUserStorage.getUsers().get(request);
    }
}
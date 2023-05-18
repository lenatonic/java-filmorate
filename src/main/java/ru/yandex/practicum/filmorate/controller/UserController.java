package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(value = "/users")
    public User addUser(@Valid @RequestBody User user) {
        log.debug("Создан новый пользователь: {}", user);
        return userService.addUser(user);
    }

    @PutMapping(value = "/users")
    public User updateUser(@Valid @RequestBody User user) {
        User request = userService.updateUser(user);
        log.debug("Обновлены данные для пользователя: {}", user.getId());
        return request;
    }

    @GetMapping(value = "/users")
    public List<User> findAllUser() {
        log.debug("Находим список всех пользователей: ");
        return userService.findAllUser();
    }

    @GetMapping(value = "/users/{id}")
    public User findUser(@PathVariable("id") Long id) {
        User request = userService.findUser(id);
        log.debug("Находим пользователя по id: " + id);
        return request;
    }

    @PutMapping(value = "/users/{id}/friends/{friendId}")
    public User addFriend(@PathVariable("id") Long id,
                          @PathVariable("friendId") Long friendId) {
        User request = userService.addFriend(id, friendId);
        log.debug("Добавляем user с id: " + friendId + " в друзья к user " + id);
        return request;
    }

    @GetMapping(value = "/users/{id}/friends")
    public List<User> findAllFriends(@PathVariable("id") Long id) {
        List<User> request = userService.findAllFriends(id);
        log.debug("Список друзей пользователя с id: " + id);
        return request;
    }

    @GetMapping(value = "/users/{id}/friends/common/{otherId}")
    public List<User> findCommonFriends(@PathVariable("id") Long id,
                                        @PathVariable("otherId") Long otherId) {
        List<User> request = userService.findCommonFriends(id, otherId);
        log.debug("Список общих друзей пользователей с id: " + id + " и " + otherId);
        return request;
    }

    @DeleteMapping(value = "/users/{id}/friends/{friendId}")
    public User deleteFriend(@PathVariable("id") Long id,
                             @PathVariable("friendId") Long friendId) {
        User request = userService.deleteFriend(id, friendId);
        log.debug("Удаляем user с id: " + friendId + " из друзей user " + id);
        return request;
    }
}
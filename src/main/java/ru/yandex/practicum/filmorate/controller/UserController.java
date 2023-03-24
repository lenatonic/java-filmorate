package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;

@Slf4j
@RestController
public class UserController {
    private final HashMap<Integer, User> users = new HashMap<>();
    private int id = 0;

    @PostMapping(value = "/users")
    public User addUser(@Valid @RequestBody User user) {
        validationUserName(user);

        user.setId(++id);
        users.put(id, user);
        log.debug("Создан новый пользователь: ", user);
        return user;
    }

    @PutMapping(value = "/users")
    public User updateUser(@Valid @RequestBody User user) {

        if (!users.containsKey(user.getId())) {
            throw new NotFoundException("Неверно указан id");
        }
        validationUserName(user);
        users.put(user.getId(), user);

        log.debug("Обновлены данные для пользователя: ", users.get(user.getId()));
        return user;
    }

    @GetMapping(value = "/users")
    public ArrayList<User> findAllUser() {
        return new ArrayList<>(users.values());
    }

    public void validationUserName(User user) {
        if (user.getName() == null || user.getName() == " " || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }

    public HashMap<Integer, User> getUsers() {
        return users;
    }
}

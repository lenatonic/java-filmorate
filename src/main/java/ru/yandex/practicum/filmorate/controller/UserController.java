package ru.yandex.practicum.filmorate.controller;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;

@JsonFormat(pattern = "yyyy-MM-dd")
@Slf4j
@RestController
@Data
public class UserController {
    private final HashMap<Integer, User> users = new HashMap<>();
    int id = 1;

    @PostMapping(value = "/users")
    public User addUser(@RequestBody User user) throws ValidationException {
        validationUser(user);
        for (User user1 : users.values()) {
            if (user.equals(user1)) {
                return user1;
            }
        }
        user.setId(id);
        users.put(id, user);
        log.debug("Создан новый пользователь: ", user);
        id++;
        return user;
    }

    @PutMapping(value = "/users")
    public User updateUser(@RequestBody User user) throws ValidationException, IOException {

        if (!users.containsKey(user.getId())) {
            throw new ValidationException("Неверно указан id");
        }
        validationUser(user);
        users.put(user.getId(), user);

        log.debug("Обновлены данные для пользователя: ", users.get(user.getId()));
        return users.get(user.getId());
    }

    @GetMapping(value = "/users")
    public ArrayList<User> findAllUser() {
        ArrayList<User> userList = new ArrayList<>();
        users.keySet().stream()
                .forEach((id) -> userList.add(users.get(id)));
        return userList;
    }

    public void validationUser(User user) throws ValidationException {
        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        if (user.getName() == null || user.getName() == " " || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        if (user.getEmail() == null || !user.getEmail().contains("@")) {
            log.error("email задан не верно");
            throw new ValidationException("Email не задан или не содержит символ @");
        }
        if (user.getLogin() == null || user.getLogin().contains(" ") || user.getLogin().isBlank()) {
            log.error("login задан не верно");
            throw new ValidationException("Логин не задан или содержит пробелы");
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            log.error("birthdayData задана не верно");
            throw new ValidationException("Дата рождения не корректна. Это не может быть будущее");
        }
    }
}

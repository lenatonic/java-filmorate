package ru.yandex.practicum.filmorate.storage;

import lombok.Data;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

@Component
@Data
public class InMemoryUserStorage implements UserStorage {
    private HashMap<Long, User> users = new HashMap<>();
    private Long id = 0L;

    @Override
    public User addUser(User user) {
        validationUserName(user);
        user.setId(++id);
        User addedUser = user;
        addedUser.setFriends(new HashSet<>());
        users.put(user.getId(), addedUser);
        return user;
    }

    @Override
    public User updateUser(User user) {
        if (!users.containsKey(user.getId())) {
            throw new NotFoundException("Неверно указан id");
        }
        validationUserName(user);
        User updatedUser = user;
        updatedUser.setFriends(users.get(user.getId()).getFriends());

        users.put(user.getId(), updatedUser);
        return user;
    }

    @Override
    public ArrayList<User> findAllUser() {
        return new ArrayList<>(users.values());
    }

    public void validationUserName(User user) {
        if (user.getName().equals(null) || user.getName().equals(" ") || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }

    public User findUser(Long id) {
        if (!users.containsKey(id)) {
            throw new NotFoundException("Нет пользователя с таким id");
        } else {
            return users.get(id);
        }
    }
}
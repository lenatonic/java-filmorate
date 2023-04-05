package ru.yandex.practicum.filmorate.storage;

import lombok.Data;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;

@Component
@Data
public class InMemoryUserStorage implements UserStorage {
    private final HashMap<Long, User> users = new HashMap<>();
    private Long id = 0L;

    @Override
    public User addUser(User user) {
        validationUserName(user);
        user.setId(++id);
        users.put(id, user);
        return user;
    }

    @Override
    public User updateUser(User user) {
        if (!users.containsKey(user.getId())) {
            throw new NotFoundException("Неверно указан id");
        }
        validationUserName(user);
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public ArrayList<User> findAllUser() {
        return new ArrayList<>(users.values());
    }

    public void validationUserName(User user) {
        if (user.getName() == null || user.getName() == " " || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }

    public void validationIdUser(Long id) {
        if (!users.containsKey(id)) {
            throw new NotFoundException("Нет пользователя с таким id");
        }
    }
}
package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserControllerTest {
    private UserController userController = new UserController();

    @Test
    public void shouldReturnUserAfterMethodAddUser() {

        User testUser = User.builder()
                .name("newUser")
                .email("newUser@mail.ru")
                .login("loginUser")
                .birthday(LocalDate.of(2010, 10, 10))
                .build();

        userController.addUser(testUser);

        assertEquals(testUser.toString(), userController.getUsers().get(1).toString());
    }

    @Test
    public void shouldReturnUserAfterMethodAddUserIfNameEmpty() {
        User testUser = User.builder()
                .name(null)
                .email("newUser@mail.ru")
                .login("loginUser")
                .birthday(LocalDate.of(2010, 10, 10))
                .build();

        userController.addUser(testUser);

        User validateUser = User.builder()
                .id(1)
                .name("loginUser")
                .email("newUser@mail.ru")
                .login("loginUser")
                .birthday(LocalDate.of(2010, 10, 10))
                .build();

        assertEquals(validateUser.toString(), userController.getUsers().get(1).toString());
    }

    @Test
    public void shouldReturnUserAfterMethodUpdateUser() {
        User testUser = User.builder()
                .name("newUser")
                .email("newUser@mail.ru")
                .login("loginUser")
                .birthday(LocalDate.of(2010, 10, 10))
                .build();

        userController.addUser(testUser);

        User updateUser = User.builder()
                .id(1)
                .name("updateUser")
                .email("updateUser@mail.ru")
                .login("loginUpdateUser")
                .birthday(LocalDate.of(2010, 10, 12))
                .build();
        userController.updateUser(updateUser);

        assertEquals(updateUser.toString(), userController.getUsers().get(1).toString());
    }

    @Test
    public void shouldReturnUserAfterMethodUpdateUserIfNameEmpty() {
        User testUser = User.builder()
                .name("newUser")
                .email("newUser@mail.ru")
                .login("loginUser")
                .birthday(LocalDate.of(2010, 10, 10))
                .build();

        userController.addUser(testUser);

        User updateUser = User.builder()
                .id(1)
                .name("")
                .email("newUser@mail.ru")
                .login("loginUpdateUser")
                .birthday(LocalDate.of(2010, 10, 10))
                .build();

        userController.updateUser(updateUser);

        User validateUser = User.builder()
                .id(1)
                .name("loginUpdateUser")
                .email("newUser@mail.ru")
                .login("loginUpdateUser")
                .birthday(LocalDate.of(2010, 10, 10))
                .build();

        assertEquals(validateUser.toString(), userController.getUsers().get(1).toString());
    }

    @Test
    public void shouldReturnAllUserAfterMethodUpdateUser() {
        User testUser = User.builder()
                .name("newUser")
                .email("newUser@mail.ru")
                .login("loginUser")
                .birthday(LocalDate.of(2010, 10, 10))
                .build();

        userController.addUser(testUser);

        User updateUser = User.builder()
                .id(1)
                .name("updateUser")
                .email("newUpdateUser@mail.ru")
                .login("loginUpdateUser")
                .birthday(LocalDate.of(2010, 10, 10))
                .build();

        userController.updateUser(updateUser);

        assertEquals(updateUser.toString(), userController.findAllUser().toString()
                .replace("[", "").replace("]", ""));
    }
}

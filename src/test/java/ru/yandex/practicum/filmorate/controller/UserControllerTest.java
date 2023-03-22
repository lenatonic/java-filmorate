package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.io.IOException;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class UserControllerTest {
    private UserController userController = new UserController();

    @Test
    public void shouldReturnUserAfterMethodAddUser() throws ValidationException {

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
    public void shouldThrowExceptionAfterMethodAddUserIfEmailEmpty() throws ValidationException {
        User testUser = User.builder()
                .name("newUser")
                .email(null)
                .login("loginUser")
                .birthday(LocalDate.of(2010, 10, 10))
                .build();

        Executable executable = () -> userController.addUser(testUser);
        final ValidationException exception = assertThrows(
                ValidationException.class, executable);

        assertEquals("Email не задан или не содержит символ @", exception.getMessage());
    }

    @Test
    public void shouldThrowExceptionAfterMethodAddUserIfEmailWithoutChar() throws ValidationException {
        User testUser = User.builder()
                .name("newUser")
                .email("newUser.mail.ru")
                .login("loginUser")
                .birthday(LocalDate.of(2010, 10, 10))
                .build();

        Executable executable = () -> userController.addUser(testUser);
        final ValidationException exception = assertThrows(
                ValidationException.class, executable);

        assertEquals("Email не задан или не содержит символ @", exception.getMessage());
    }

    @Test
    public void shouldThrowExceptionAfterMethodAddUserIfLoginNull() throws ValidationException {
        User testUser = User.builder()
                .name("newUser")
                .email("newUser@mail.ru")
                .login(null)
                .birthday(LocalDate.of(2010, 10, 10))
                .build();

        Executable executable = () -> userController.addUser(testUser);
        final ValidationException exception = assertThrows(
                ValidationException.class, executable);

        assertEquals("Логин не задан или содержит пробелы", exception.getMessage());
    }

    @Test
    public void shouldThrowExceptionAfterMethodAddUserIfLoginIsBlankOrSpace() throws ValidationException {
        User testUser = User.builder()
                .name("newUser")
                .email("newUser@mail.ru")
                .login(" ")
                .birthday(LocalDate.of(2010, 10, 10))
                .build();

        Executable executable = () -> userController.addUser(testUser);
        final ValidationException exception = assertThrows(
                ValidationException.class, executable);

        assertEquals("Логин не задан или содержит пробелы", exception.getMessage());
    }

    @Test
    public void shouldReturnUserAfterMethodAddUserIfNameEmpty() throws ValidationException {
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
    public void shouldThrowExceptionAfterMethodAddUserIfBirthdayFuture() throws ValidationException {
        User testUser = User.builder()
                .name("newUser")
                .email("newUser@mail.ru")
                .login("loginUser")
                .birthday(LocalDate.of(2025, 10, 10))
                .build();

        Executable executable = () -> userController.addUser(testUser);
        final ValidationException exception = assertThrows(
                ValidationException.class, executable);

        assertEquals("Дата рождения не корректна. Это не может быть будущее", exception.getMessage());
    }

    @Test
    public void shouldReturnUserAfterMethodUpdateUser() throws ValidationException, IOException {
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
    public void shouldThrowExceptionAfterMethodUpdateUserIfEmailNull() throws ValidationException {
        User testUser = User.builder()
                .name("newUser")
                .email("newUser@mail.ru")
                .login("loginUser")
                .birthday(LocalDate.of(2010, 10, 10))
                .build();

        userController.addUser(testUser);

        User updateUser = User.builder()
                .id(1)
                .name("newUser")
                .email(null)
                .login("loginUser")
                .birthday(LocalDate.of(2010, 10, 10))
                .build();

        Executable executable = () -> userController.updateUser(updateUser);
        final ValidationException exception = assertThrows(
                ValidationException.class, executable);

        assertEquals("Email не задан или не содержит символ @", exception.getMessage());
    }

    @Test
    public void shouldThrowExceptionAfterMethodUpdateUserIfEmailNoCharEmail() throws ValidationException {
        User testUser = User.builder()
                .name("newUser")
                .email("newUser@mail.ru")
                .login("loginUser")
                .birthday(LocalDate.of(2010, 10, 10))
                .build();

        userController.addUser(testUser);

        User updateUser = User.builder()
                .id(1)
                .name("newUser")
                .email("newUser.mail.ru")
                .login("loginUser")
                .birthday(LocalDate.of(2010, 10, 10))
                .build();

        Executable executable = () -> userController.updateUser(updateUser);
        final ValidationException exception = assertThrows(
                ValidationException.class, executable);

        assertEquals("Email не задан или не содержит символ @", exception.getMessage());
    }

    @Test
    public void shouldThrowExceptionAfterMethodUpdateUserIfLoginNull() throws ValidationException {
        User testUser = User.builder()
                .name("newUser")
                .email("newUser@mail.ru")
                .login("loginUser")
                .birthday(LocalDate.of(2010, 10, 10))
                .build();

        userController.addUser(testUser);

        User updateUser = User.builder()
                .id(1)
                .name("newUser")
                .email("newUser@mail.ru")
                .login(null)
                .birthday(LocalDate.of(2010, 10, 10))
                .build();

        Executable executable = () -> userController.updateUser(updateUser);
        final ValidationException exception = assertThrows(
                ValidationException.class, executable);

        assertEquals("Логин не задан или содержит пробелы", exception.getMessage());
    }

    @Test
    public void shouldThrowExceptionAfterMethodUpdateUserIfLoginIsBlankOrSpace() throws ValidationException {
        User testUser = User.builder()
                .name("newUser")
                .email("newUser@mail.ru")
                .login("loginUser")
                .birthday(LocalDate.of(2010, 10, 10))
                .build();

        userController.addUser(testUser);

        User updateUser = User.builder()
                .id(1)
                .name("newUser")
                .email("newUser@mail.ru")
                .login(" ")
                .birthday(LocalDate.of(2010, 10, 10))
                .build();

        Executable executable = () -> userController.updateUser(updateUser);
        final ValidationException exception = assertThrows(
                ValidationException.class, executable);

        assertEquals("Логин не задан или содержит пробелы", exception.getMessage());
    }

    @Test
    public void shouldReturnUserAfterMethodUpdateUserIfNameEmpty() throws ValidationException, IOException {
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
    public void shouldThrowExceptionAfterMethodUpdateUserIfBirthdayFuture() throws ValidationException {
        User testUser = User.builder()
                .name("newUser")
                .email("newUser@mail.ru")
                .login("loginUser")
                .birthday(LocalDate.of(2010, 10, 10))
                .build();

        userController.addUser(testUser);

        User updateUser = User.builder()
                .id(1)
                .name("newUser")
                .email("newUser@mail.ru")
                .login("loginUpdateUser")
                .birthday(LocalDate.of(2025, 10, 10))
                .build();

        Executable executable = () -> userController.updateUser(updateUser);
        final ValidationException exception = assertThrows(
                ValidationException.class, executable);

        assertEquals("Дата рождения не корректна. Это не может быть будущее", exception.getMessage());
    }

    @Test
    public void shouldReturnAllUserAfterMethodUpdateUser() throws ValidationException, IOException {
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

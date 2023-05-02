
package ru.yandex.practicum.filmorate.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

//public class UserControllerTest {
//    private InMemoryUserStorage inMemoryUserStorage = new InMemoryUserStorage();
//    private UserService userService = new UserService(inMemoryUserStorage);
//    private UserController userController = new UserController(userService);
//
//    @Test
//    public void shouldReturnUserAfterMethodAddUser() {
//
//        User testUser = User.builder()
//                .name("newUser")
//                .email("newUser@mail.ru")
//                .login("loginUser")
//                .birthday(LocalDate.of(2010, 10, 10))
//                .friends(new HashSet<>())
//                .build();
//
//        User addedUser = userController.addUser(testUser);
//
//        assertEquals(testUser.toString(), addedUser.toString());
//    }
//
//    @Test
//    public void shouldReturnUserAfterMethodAddUserIfNameEmpty() {
//        User testUser = User.builder()
//                .name("")
//                .email("newUser@mail.ru")
//                .login("loginUser")
//                .birthday(LocalDate.of(2010, 10, 10))
//                .build();
//
//        User addedUser = userController.addUser(testUser);
//
//        User validateUser = User.builder()
//                .id(1L)
//                .name("loginUser")
//                .email("newUser@mail.ru")
//                .login("loginUser")
//                .birthday(LocalDate.of(2010, 10, 10))
//                .friends(new HashSet<>())
//                .build();
//
//        assertEquals(validateUser.toString(), addedUser.toString());
//    }
//
//    @Test
//    public void shouldReturnUserAfterMethodUpdateUser() {
//        User testUser = User.builder()
//                .name("newUser")
//                .email("newUser@mail.ru")
//                .login("loginUser")
//                .birthday(LocalDate.of(2010, 10, 10))
//                .build();
//
//        userController.addUser(testUser);
//
//        User updateUser = User.builder()
//                .id(1L)
//                .name("updateUser")
//                .email("updateUser@mail.ru")
//                .login("loginUpdateUser")
//                .birthday(LocalDate.of(2010, 10, 12))
//                .build();
//        User addedUser = userController.updateUser(updateUser);
//
//        assertEquals(updateUser.toString(), addedUser.toString());
//    }
//
//    @Test
//    public void shouldReturnUserAfterMethodUpdateUserIfNameEmpty() {
//        User testUser = User.builder()
//                .name("newUser")
//                .email("newUser@mail.ru")
//                .login("loginUser")
//                .birthday(LocalDate.of(2010, 10, 10))
//                .build();
//
//        userController.addUser(testUser);
//
//        User updateUser = User.builder()
//                .id(1L)
//                .name("")
//                .email("newUser@mail.ru")
//                .login("loginUpdateUser")
//                .birthday(LocalDate.of(2010, 10, 10))
//                .build();
//
//        User addedUser = userController.updateUser(updateUser);
//
//        User validateUser = User.builder()
//                .id(1L)
//                .name("loginUpdateUser")
//                .email("newUser@mail.ru")
//                .login("loginUpdateUser")
//                .birthday(LocalDate.of(2010, 10, 10))
//                .friends(new HashSet<>())
//                .build();
//
//        assertEquals(validateUser.toString(), addedUser.toString());
//    }
//
//    @Test
//    public void shouldReturnAllUserAfterMethodUpdateUser() {
//        User testUser = User.builder()
//                .name("newUser")
//                .email("newUser@mail.ru")
//                .login("loginUser")
//                .birthday(LocalDate.of(2010, 10, 10))
//                .build();
//
//        userController.addUser(testUser);
//
//        User updateUser = User.builder()
//                .id(1L)
//                .name("updateUser")
//                .email("newUpdateUser@mail.ru")
//                .login("loginUpdateUser")
//                .birthday(LocalDate.of(2010, 10, 10))
//                .build();
//
//        User addedUser = userController.updateUser(updateUser);
//
//        assertEquals(updateUser.toString(), addedUser.toString());
//    }
//}